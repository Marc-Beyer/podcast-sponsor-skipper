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
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.util.Constants
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class UpdateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val podcastsUseCases: PodcastsUseCases
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

            while (shouldWork) {
                val currentUrl = UpdateManager.decrement()
                updateNotification(notificationManager, title)
                Log.i("Work","currentUrl $currentUrl")
                if (currentUrl == null) {
                    break
                }
                podcastsUseCases.getRSSFeed(currentUrl).firstOrNull()?.let {
                    podcastsUseCases.insertPodcastUseCase(it)
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
        if (activeUpdates.second == 0) {
            notificationManager.cancel(Constants.UPDATE_NOTIFICATION_ID)
            return
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, Constants.UPDATE_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_sync)
                .setContentTitle(

                    if (activeUpdates.second == 1) {
                        "Updating $title"
                    } else {
                        "Updating podcasts (${activeUpdates.second - activeUpdates.first}/${activeUpdates.second})"
                    }
                )
                .setContentText(activeUpdates.third)
                .setStyle(NotificationCompat.BigTextStyle().bigText(activeUpdates.third))
                .setOngoing(activeUpdates.first > 0)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(
                    activeUpdates.second,
                    activeUpdates.second - activeUpdates.first,
                    true
                )

        notificationManager.notify(
            Constants.UPDATE_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }
}

object UpdateManager {
    private var activeUpdateUrl: String? = null
    private val activeUpdateUrls = mutableListOf<String>()
    private val activeUpdateTitles = mutableListOf<String>()

    private val lock = Any()

    // Return if the worker should start the work or stop
    fun increment(url: String, title: String): Boolean {
        synchronized(lock) {
            if (activeUpdateUrls.contains(url)) {
                return false
            }
            activeUpdateTitles.add(title)
            activeUpdateUrls.add(url)
            Log.i("AAA", "activeDownloadTitles.size ${activeUpdateTitles.size}")
            return activeUpdateTitles.size == 1
        }
    }

    fun decrement(): String? {
        synchronized(lock) {
            if (activeUpdateUrls.size > 0) {
                val url = activeUpdateUrls.first()
                activeUpdateUrls.removeAt(0)
                activeUpdateUrl = url
                return url
            } else {
                activeUpdateUrl = null
                activeUpdateTitles.clear()
                return null
            }
        }
    }

    fun cancel(url: String, title: String): Boolean {
        synchronized(lock) {
            if (activeUpdateUrls.contains(url)) {
                activeUpdateUrls.remove(url)
                activeUpdateTitles.remove(title)
                return true
            }
            return false
        }
    }

    fun getActiveUpdates(): Triple<Int, Int, String> {
        synchronized(lock) {
            return Triple<Int, Int, String>(
                activeUpdateUrls.size,
                activeUpdateTitles.size,
                activeUpdateTitles.joinToString("\n")
            )
        }
    }

    fun getActiveUpdateUrls(): List<String> {
        synchronized(lock) {
            return if (activeUpdateUrl != null) {
                listOf(activeUpdateUrl!!) + activeUpdateUrls.toList()
            } else {
                activeUpdateUrls.toList()
            }
        }
    }
}