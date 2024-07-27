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
import java.util.UUID

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

        val notificationId = UUID.randomUUID().hashCode()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "DOWNLOAD_CHANNEL_ID")
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle("Downloading '$title'")
            .setContentText("Download in progress")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            //.setProgress(100, 0, true)

        // Display the notification
        notificationManager.notify(notificationId, notificationBuilder.build())

        return try {
            episodeUseCases.downloadEpisodeUseCase(url)

            notificationBuilder.setContentText("Download complete")
                .setOngoing(false)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
            notificationManager.notify(notificationId, notificationBuilder.build())

            notificationManager.cancel(notificationId)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            notificationManager.cancel(notificationId)
            Result.failure()
        }
    }
}