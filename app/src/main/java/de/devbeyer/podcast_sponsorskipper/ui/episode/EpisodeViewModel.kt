package de.devbeyer.podcast_sponsorskipper.ui.episode

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.episode.EpisodeUseCases
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val episodeUseCases: EpisodeUseCases
) : ViewModel() {
    private val _state = mutableStateOf(EpisodeState())
    val state: State<EpisodeState> = _state

    fun onEvent(event: EpisodeEvent) {
        when (event) {
            is EpisodeEvent.Play -> {

            }
        }
    }


    fun setEpisode(episode: Episode, podcastWithRelations: PodcastWithRelations) {
        viewModelScope.launch {
            val sponsorSections = episodeUseCases.getSponsorSectionsUseCase(episode.episodeUrl).firstOrNull()
            _state.value = state.value.copy(
                episode = episode,
                podcastWithRelations = podcastWithRelations,
                sponsorSections = sponsorSections ?: emptyList(),
            )
        }
    }
}