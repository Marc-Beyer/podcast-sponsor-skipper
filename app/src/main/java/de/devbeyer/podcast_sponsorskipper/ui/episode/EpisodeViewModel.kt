package de.devbeyer.podcast_sponsorskipper.ui.episode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.podcast.PodcastsUseCases
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val podcastUseCase: PodcastsUseCases
) : ViewModel() {
    private val _state = mutableStateOf(EpisodeState())
    val state: State<EpisodeState> = _state

    fun onEvent(event: EpisodeEvent) {
        when (event) {
            is EpisodeEvent.Play -> {

            }
        }
    }


    fun setEpisode(episode: Episode) {
        _state.value = state.value.copy(
            episode = episode,
        )
    }
}