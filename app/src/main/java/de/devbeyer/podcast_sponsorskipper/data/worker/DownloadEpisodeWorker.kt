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
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.util.Constants

@HiltWorker
class DownloadEpisodeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val episodeUseCases: EpisodeUseCases
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()
        val title = inputData.getString("title") ?: return Result.failure()
        Log.i("AAA", "EPISODE WORKER $url $title")


        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        /*
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "DOWNLOAD_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading '$title'")
            .setContentText("Download in progress")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(100, 0, true)

        // Display the notification
        //notificationManager.notify(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID, notificationBuilder.build())
         */

        return try {
            val shouldWork = DownloadManager.increment(url = url, title = title)
            updateNotification(notificationManager, title)

            while (shouldWork) {
                val currentUrl = DownloadManager.decrement()
                if(currentUrl == null){
                    updateNotification(notificationManager, title)
                    break
                }
                Log.i("AAA", "EPISODE WORKER START WORKING ON $currentUrl $title")

                episodeUseCases.downloadEpisodeUseCase(currentUrl)
                episodeUseCases.downloadSponsorSectionsUseCase(currentUrl)
                Log.i("AAA", "EPISODE WORKER FINISHED WORKING LETS GET THE NEXT $title")
            }
            Log.i("AAA", "EPISODE WORKER QUIT $title")

            /*
            notificationBuilder.setContentText("Download complete")
                .setOngoing(false)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
            notificationManager.notify(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID, notificationBuilder.build())

            notificationManager.cancel(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID)
             */

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            notificationManager.cancel(Constants.DOWNLOAD_EPISODE_NOTIFICATION_ID)
            Result.failure()
        }
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

data class DownloadState(
    val activeDownloads: Int,
    val age: Int,
    val address: String,
    val phoneNumber: String
)

object DownloadManager {
    private val activeDownloadUrls = mutableListOf<String>()
    private val activeDownloadTitles = mutableListOf<String>()
    private val lock = Any()

    // Return if the worker should start the work or stop
    fun increment(url: String, title: String): Boolean {
        synchronized(lock) {
            if (activeDownloadUrls.contains(url)) {
                return false
            }
            activeDownloadTitles.add(title)
            activeDownloadUrls.add(url)
            Log.i("AAA", "activeDownloadTitles.size ${activeDownloadTitles.size}")
            return activeDownloadTitles.size == 1
        }
    }

    fun decrement(): String? {
        synchronized(lock) {
            if (activeDownloadUrls.size > 0) {
                val url = activeDownloadUrls.first()
                activeDownloadUrls.removeAt(0)
                return url
            } else {
                activeDownloadTitles.clear()
                return null
            }
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
}