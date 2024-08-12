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
import kotlinx.coroutines.delay

@HiltWorker
class DownloadEpisodeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val episodeUseCases: EpisodeUseCases,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()
        val title = inputData.getString("title") ?: return Result.failure()
        Log.i("AAA", "EPISODE WORKER $url $title")


        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return try {
            //val shouldWork = DownloadManager.increment(url = url, title = title)
            updateNotification(notificationManager, title)

            while (true) {
                val currentUrl = DownloadManager.decrement()
                updateNotification(notificationManager, title)
                if (currentUrl == null) {
                    break
                }
                try {
                    episodeUseCases.downloadEpisodeUseCase(currentUrl)
                }catch (e: Exception){
                    Log.i("AAA", "ERROR $e")
                }
                Log.i("AAA", "DELAY START")
                delay(5000)
                Log.i("AAA", "DELAY END")
                //episodeUseCases.downloadSponsorSectionsUseCase(currentUrl, -1)
            }

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

object DownloadManager {
    private var activeDownloadUrl: String? = null
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
                activeDownloadUrl = url
                return url
            } else {
                activeDownloadUrl = null
                activeDownloadTitles.clear()
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
}