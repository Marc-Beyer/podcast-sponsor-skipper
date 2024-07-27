package de.devbeyer.podcast_sponsorskipper.ui.navigation

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.work.WorkManager
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import de.devbeyer.podcast_sponsorskipper.service.PlaybackService
import javax.inject.Inject


@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val podcastsUseCases: PodcastsUseCases,
    private val episodeUseCases: EpisodeUseCases,
    private val workManager: WorkManager,
    private val application: Application,
) : ViewModel() {
    private val _state = mutableStateOf(NavigationState())
    val state: State<NavigationState> = _state

    fun onEvent(event: NavigationEvent) {
        when (event) {
            is NavigationEvent.PlayEpisode -> {
                Log.i("AAA", "Play ${event.episode.title}")
                val mediaItem =
                    MediaItem.Builder()
                        .setMediaId("media-1")
                        .setUri(event.episode.episodePath)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setArtist(event.episode.podcastId.toString())
                                .setTitle(event.episode.title)
                                .setArtworkUri(
                                    Uri.parse(
                                        event.episode.imagePath ?: event.episode.imageUrl
                                    )
                                )
                                .build()
                        )
                        .build()

                mediaController?.setMediaItem(mediaItem)
                mediaController?.prepare()
                mediaController?.play()

            }
        }

    }

    init {
        initializeMediaController(context = application)
    }

    var mediaController: MediaController? = null
        private set

    fun initializeMediaController(context: Context) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                mediaController = controllerFuture.get()
                mediaController?.addListener(object : Player.Listener {
                    // Implement listener methods here
                })
            },
            MoreExecutors.directExecutor()
        )
    }
}