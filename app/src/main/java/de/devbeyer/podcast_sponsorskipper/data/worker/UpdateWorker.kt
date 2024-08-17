package de.devbeyer.podcast_sponsorskipper.data.worker

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import de.devbeyer.podcast_sponsorskipper.R
import de.devbeyer.podcast_sponsorskipper.domain.models.Settings
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SettingsUseCases
import de.devbeyer.podcast_sponsorskipper.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val podcastsUseCases: PodcastsUseCases,
    private val settingsUseCases: SettingsUseCases,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()
        val title = inputData.getString("title") ?: return Result.failure()
        Log.i("AAA", "WORKER $title $url")

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return try {
            val shouldWork = UpdateManager.increment(url = url, title = title)
            updateNotification(notificationManager, title)

            val settings = if (shouldWork) {
                settingsUseCases.getSettingsUseCase().firstOrNull() ?: Settings()
            } else {
                Settings()
            }

            var newEpisodes = 0
            while (shouldWork) {
                val currentUpdate = UpdateManager.decrement(newEpisodes = newEpisodes, onDone = {
                    updateNotification(notificationManager, title)
                })
                val currentUrl = currentUpdate?.first
                Log.i("Work", "currentUrl $currentUrl")
                if (currentUpdate == null || currentUrl == null) {
                    break
                }
                updateNotification(notificationManager, title)

                try {
                    podcastsUseCases.getRSSFeedUseCase(currentUrl).firstOrNull()?.let {
                        newEpisodes = podcastsUseCases.insertPodcastUseCase(
                            podcastAndEpisodes = it,
                            downloadImages = settings.downloadImages,
                            coverImageSize = settings.coverImageSize,
                        )
                        updateNotification(notificationManager, title)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    newEpisodes = 0
                }
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            notificationManager.cancel(Constants.UPDATE_NOTIFICATION_ID)
            Result.failure()
        }
    }

    private fun updateNotification(notificationManager: NotificationManager, title: String) {
        val activeUpdates = UpdateManager.getActiveUpdates()
        val complete = activeUpdates.nrOfUpdated == activeUpdates.nrOfUpdates
        if (activeUpdates.text.isBlank()) {
            notificationManager.cancel(Constants.UPDATE_NOTIFICATION_ID)
            return
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, Constants.UPDATE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(
                    if (complete) {
                        if (activeUpdates.text.isBlank()) {
                            "No new episodes"
                        } else {
                            "${activeUpdates.newEpisodes} new episodes"
                        }

                    } else
                        if (activeUpdates.nrOfUpdates == 1) {
                            "Updating $title"
                        } else {
                            "Updating podcasts (${activeUpdates.nrOfUpdated + 1}/${activeUpdates.nrOfUpdates})"
                        }
                )
                .setOngoing(!complete)
                .setPriority(NotificationCompat.PRIORITY_LOW)

        if (!complete) {
            notificationBuilder
                .setSmallIcon(android.R.drawable.ic_popup_sync)
                .setContentText(activeUpdates.text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(activeUpdates.text))
                .setProgress(
                    activeUpdates.nrOfUpdates,
                    activeUpdates.nrOfUpdates - activeUpdates.nrOfUpdated,
                    true
                )
        } else {
            notificationBuilder
                .setContentText(activeUpdates.text)

        }

        notificationManager.notify(
            Constants.UPDATE_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }
}

object UpdateManager {
    private var activeUpdateUrl: Int = -1

    //private val activeUpdateUrls = mutableListOf<String>()
    //private val activeUpdateTitles = mutableListOf<String>()
    private val activeUpdates = mutableListOf<Triple<String, String, Int>>()

    private val lock = Any()

    private val _activeUpdateUrlsFlow = MutableStateFlow<List<String>>(emptyList())
    val activeUpdateUrlsFlow: StateFlow<List<String>> get() = _activeUpdateUrlsFlow

    // Return if the worker should start the work or stop
    fun increment(url: String, title: String): Boolean {
        synchronized(lock) {
            val newUpdate = Triple(url, title, -1)
            if (
                activeUpdates.any {
                    it.first == url
                }
            ) {
                return false
            }
            //activeUpdateTitles.add(title)
            //activeUpdateUrls.add(url)
            activeUpdates.add(newUpdate)
            Log.i("AAA", "activeDownloadTitles.size ${activeUpdates.size}")
            updateActiveUpdateUrls()
            return activeUpdates.size == 1
        }
    }

    fun decrement(newEpisodes: Int, onDone: () -> Unit): Triple<String, String, Int>? {
        synchronized(lock) {
            activeUpdateUrl++
            if (activeUpdateUrl > 0 && activeUpdates.size > activeUpdateUrl - 1) {
                activeUpdates[activeUpdateUrl - 1] = activeUpdates[activeUpdateUrl - 1].copy(
                    third = newEpisodes
                )
            }
            updateActiveUpdateUrls()

            if (activeUpdates.size > activeUpdateUrl) {
                val activeUpdate = activeUpdates[activeUpdateUrl]
                return activeUpdate
            } else {
                onDone()
                //activeUpdateTitles.clear()
                activeUpdateUrl = -1
                activeUpdates.clear()
                return null
            }
        }
    }

    fun cancel(url: String, title: String): Boolean {
        synchronized(lock) {
            val index = activeUpdates.indexOfFirst { it.first == url }

            if (index <= activeUpdateUrl) return false
            if (index >= 0) {
                //activeUpdateUrls.remove(url)
                //activeUpdateTitles.remove(title)
                updateActiveUpdateUrls()
                return true
            }
            return false
        }
    }

    fun getActiveUpdates(): ActiveUpdatesData {
        synchronized(lock) {
            val relevantUpdates = activeUpdates.filter { it.third != 0 }
            return ActiveUpdatesData(
                nrOfUpdated = activeUpdateUrl,
                nrOfUpdates = activeUpdates.size,
                text = relevantUpdates.joinToString("\n") {
                    it.second + if (it.third >= 0) " ${it.third} new episodes" else ""
                },
                newEpisodes = relevantUpdates.sumOf { it.third }
            )
        }
    }

    private fun getActiveUpdateUrls(): List<String> {
        synchronized(lock) {
            return activeUpdates.filter { it.third == -1 }.map { it.first }
            /*
            return if (activeUpdateUrl != null) {
                listOf(activeUpdateUrl!!) + activeUpdateUrls.toList()
            } else {
                activeUpdateUrls.toList()
            }
            */
        }
    }

    private fun updateActiveUpdateUrls() {
        _activeUpdateUrlsFlow.value = getActiveUpdateUrls()
    }
}

data class ActiveUpdatesData(
    val nrOfUpdated: Int,
    val nrOfUpdates: Int,
    val text: String,
    val newEpisodes: Int,
)