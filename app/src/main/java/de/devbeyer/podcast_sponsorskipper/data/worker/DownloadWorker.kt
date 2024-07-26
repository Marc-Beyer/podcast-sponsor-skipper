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
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val podcastsUseCases: PodcastsUseCases
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()
        Log.i("AAA", "WORKER $url")

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "DOWNLOAD_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading Podcast")
            .setContentText("Download in progress")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        // Display the notification
        notificationManager.notify(1, notificationBuilder.build())

        return try {
            podcastsUseCases.getRSSFeed(url).firstOrNull()?.let {
                podcastsUseCases.insertPodcastUseCase(it)
            }

            notificationBuilder.setContentText("Download complete")
                .setOngoing(false)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
            notificationManager.notify(1, notificationBuilder.build())

            notificationManager.cancel(1)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}