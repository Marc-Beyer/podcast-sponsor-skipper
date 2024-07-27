package de.devbeyer.podcast_sponsorskipper.service

import android.content.Intent
import android.util.Log
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    private lateinit var player: ExoPlayer

    override fun onCreate() {
        Log.i("AAA", "onCreate: PlaybackService")
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
        mediaSession = MediaSession.Builder(this, player).build()
        /*
        val mediaItem = MediaItem.fromUri("https://op3.dev/e/https://cdn.changelog.com/uploads/jsparty/326/js-party-326.mp3")
        player.setMediaItem(mediaItem)
        player.prepare()
        */
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
        val player = mediaSession?.player!!
        if (!player.playWhenReady
            || player.mediaItemCount == 0
            || player.playbackState == Player.STATE_ENDED
        ) {
            stopSelf()
        }
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

}