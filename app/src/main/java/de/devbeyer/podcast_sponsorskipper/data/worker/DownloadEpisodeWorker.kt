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
import de.devbeyer.podcast_sponsorskipper.domain.models.Settings
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SettingsUseCases
import de.devbeyer.podcast_sponsorskipper.util.Constants
import de.devbeyer.podcast_sponsorskipper.util.isWifiOrNotMetered
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class DownloadEpisodeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val episodeUseCases: EpisodeUseCases,
    private val settingsUseCases: SettingsUseCases,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()
        val title = inputData.getString("title") ?: return Result.failure()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        try {
            val shouldWork = DownloadManager.increment(url = url, title = title)
            updateNotification(notificationManager, title)

            while (shouldWork) {
                val currentUrl = DownloadManager.decrement()
                updateNotification(notificationManager, title)
                if (currentUrl == null) {
                    break
                }

                val wifi = applicationContext.isWifiOrNotMetered()

                val settings = settingsUseCases.getSettingsUseCase().firstOrNull() ?: Settings()

                if (settings.onlyUseWifi && !wifi) {
                    if (DownloadManager.getRetryCount() >= Constants.MAX_RETRY_COUNT) {
                        DownloadManager.reset()
                        notificationManager.cancel(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID)
                        Log.e("DownloadManager", "Too many retry attempts (${DownloadManager.getRetryCount()}) stopping work.")
                        return Result.failure()
                    }
                    DownloadManager.retryLater(currentUrl)
                    Log.e("DownloadManager", "Network not suitable for download. Retry Later. Retry ${DownloadManager.getRetryCount()}")

                    return Result.retry()
                }

                try {
                    episodeUseCases.downloadEpisodeUseCase(currentUrl)
                    episodeUseCases.downloadSponsorSectionsUseCase(currentUrl, -1)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            notificationManager.cancel(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID)
            return Result.failure()
        }

        return Result.success()
    }

    private fun updateNotification(notificationManager: NotificationManager, title: String) {
        val activeDownloads = DownloadManager.getActiveDownloads()

        if (activeDownloads.second == 0) {
            notificationManager.cancel(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID)
            return
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, Constants.DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(

                    if (activeDownloads.second == 1) {
                        "Downloading $title"
                    } else {
                        "Downloading podcasts (${activeDownloads.second - activeDownloads.first}/${activeDownloads.second})"
                    }
                )
                .setContentText(activeDownloads.third)
                .setStyle(NotificationCompat.BigTextStyle().bigText(activeDownloads.third))
                .setOngoing(activeDownloads.first > 0)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setProgress(
                    activeDownloads.second,
                    activeDownloads.second - activeDownloads.first,
                    true
                )

        notificationManager.notify(
            Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }
}

object DownloadManager {
    private var activeDownloadUrl: String? = null
    private val activeDownloadUrls = mutableListOf<String>()
    private val activeDownloadTitles = mutableListOf<String>()
    private var hasWorker: Boolean = false
    private var retryCount = 0
    private val lock = Any()

    // Return if the worker should start the work or stop
    fun increment(url: String, title: String): Boolean {
        synchronized(lock) {
            if (!activeDownloadUrls.contains(url)) {
                activeDownloadUrls.add(url)
            }
            if (!activeDownloadTitles.contains(title)) {
                activeDownloadTitles.add(title)
            }
            if(!hasWorker){
                hasWorker = true
                return true
            }else{
                return false
            }
        }
    }

    fun decrement(): String? {
        synchronized(lock) {
            if (activeDownloadUrls.size > 0) {
                val url = activeDownloadUrls.first()
                activeDownloadUrls.removeAt(0)
                activeDownloadUrl = url
                return url
            } else {
                reset()
                return null
            }
        }
    }

    fun cancel(url: String, title: String): Boolean {
        synchronized(lock) {
            if (activeDownloadUrls.contains(url)) {
                activeDownloadUrls.remove(url)
                activeDownloadTitles.remove(title)
                return true
            }
            return false
        }
    }

    fun reset(){
        activeDownloadUrl = null
        activeDownloadTitles.clear()
        activeDownloadUrls.clear()
        hasWorker = false
        retryCount = 0
    }

    fun retryLater(url: String){
        synchronized(lock) {
            activeDownloadUrls.add(url)
            retryCount++
            hasWorker = false
        }
    }

    fun getActiveDownloads(): Triple<Int, Int, String> {
        synchronized(lock) {
            return Triple<Int, Int, String>(
                activeDownloadUrls.size,
                activeDownloadTitles.size,
                activeDownloadTitles.joinToString("\n")
            )
        }
    }

    fun getActiveDownloadUrls(): List<String> {
        synchronized(lock) {
            return if (activeDownloadUrl != null) {
                listOf(activeDownloadUrl!!) + activeDownloadUrls.toList()
            } else {
                activeDownloadUrls.toList()
            }
        }
    }

    fun getRetryCount() = retryCount
}