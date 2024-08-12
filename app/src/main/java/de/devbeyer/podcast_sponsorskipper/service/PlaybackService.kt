package de.devbeyer.podcast_sponsorskipper.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSession.ConnectionResult
import androidx.media3.session.MediaSession.ConnectionResult.AcceptedResultBuilder
import androidx.media3.session.MediaSessionService
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import androidx.media3.ui.PlayerNotificationManager
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings.SettingsUseCases
import de.devbeyer.podcast_sponsorskipper.ui.MainActivity
import de.devbeyer.podcast_sponsorskipper.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@UnstableApi
@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    @Inject
    lateinit var episodeUseCases: EpisodeUseCases

    @Inject
    lateinit var podcastUseCases: PodcastsUseCases

    @Inject
    lateinit var settingsUseCases: SettingsUseCases

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private var mediaSession: MediaSession? = null

    private lateinit var player: ExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager

    override fun onCreate() {
        Log.i("AAA", "onCreate: PlaybackService")
        super.onCreate()

        player = ExoPlayer.Builder(this).build()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE
        )

        mediaSession = MediaSession.Builder(this, player)
            .setCallback(MediaSessionCallback())
            .setSessionActivity(pendingIntent)
            .build()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    endPodcast()
                }
            }
        })
    }

    inner class MediaSessionCallback : MediaSession.Callback {
        @OptIn(UnstableApi::class)
        override fun onConnect(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
        ): MediaSession.ConnectionResult {
            val sessionCommands = ConnectionResult.DEFAULT_SESSION_COMMANDS.buildUpon()
                .add(SessionCommand(Constants.COMMAND_SCHEDULE_EVENT, Bundle.EMPTY))
                .build()
            return AcceptedResultBuilder(session)
                .setAvailableSessionCommands(sessionCommands)
                .build()
        }

        override fun onCustomCommand(
            session: MediaSession,
            controller: MediaSession.ControllerInfo,
            customCommand: SessionCommand,
            args: Bundle,
        ): ListenableFuture<SessionResult> {
            Log.i("AAA", "customCommand.customAction ${customCommand.customAction}")
            if (customCommand.customAction == Constants.COMMAND_SCHEDULE_EVENT) {
                val startPositionMs = args.getLong("START_POSITION_MS") ?: 0L
                val endPositionMs = args.getLong("END_POSITION_MS") ?: 0L
                scheduleEventAtPosition(
                    positionMs = startPositionMs,
                    action = {
                        Log.i("AAA", "REACHED POSITION LLLLLLLLLLLLLLLLL")
                        player.seekTo(endPositionMs)
                    },
                )
            }
            return Futures.immediateFuture(
                SessionResult(SessionResult.RESULT_SUCCESS)
            )
        }
    }


    fun scheduleEventAtPosition(positionMs: Long, action: () -> Unit) {
        Log.i("AAA", "scheduleEventAtPosition $positionMs")
        player.createMessage { _, _ ->
            action()
            scheduleEventAtPosition(positionMs = positionMs, action = action)
        }
            .setLooper(Looper.getMainLooper())
            .setPosition(0, positionMs)
            .send()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i("AAA", "onStartCommand")
        player.play()
        return START_STICKY
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        Log.i("AAA", "onGetSession")
        return mediaSession
    }

    // TODO maybe I want to keep the session active even if the user dismissed the app from the recent tasks
    override fun onTaskRemoved(rootIntent: Intent?) {
        /*
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            stopSelf()
        }
         */
    }


    override fun onDestroy() {
        Log.i("AAA", "onDestroy")
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    private fun getCurrentEpisodeUrl(): String? {
        return player.currentMediaItem?.mediaMetadata?.extras?.getString("episodeUrl")
    }

    fun updateEpisodesLastPosition() {
        val currentPosition = player.currentPosition
        getCurrentEpisodeUrl()?.let { episodeUrl ->
            serviceScope.launch {
                episodeUseCases.getEpisodeUseCase(episodeUrl = episodeUrl).firstOrNull()
                    ?.let { episode ->
                        episodeUseCases.updateEpisodeUseCase(
                            episode.copy(
                                lastPosition = currentPosition
                            )
                        )
                    }
            }
        }
    }

    fun endPodcast() {
        getCurrentEpisodeUrl()?.let { episodeUrl ->
            serviceScope.launch {
                episodeUseCases.getEpisodeUseCase(episodeUrl = episodeUrl).firstOrNull()
                    ?.let { episode ->
                        podcastUseCases.getLocalPodcastByIdUseCase(episode.podcastId).firstOrNull()
                            ?.let { podcastWithRelations ->
                                settingsUseCases.getSettingsUseCase().firstOrNull()
                                    ?.let { settings ->
                                        episodeUseCases.completeEpisodeUseCase(
                                            episode = episode,
                                            podcast = podcastWithRelations.podcast,
                                            autoDeleteCompletedEpisodes = settings.autoDeleteCompletedEpisodes,
                                        )
                                    }
                            }
                    }
            }
        }
    }
}