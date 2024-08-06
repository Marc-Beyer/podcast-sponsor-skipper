package de.devbeyer.podcast_sponsorskipper.ui.navigation

import androidx.media3.session.MediaController
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.PodcastWithRelations
import de.devbeyer.podcast_sponsorskipper.domain.models.db.SponsorSection

data class NavigationState (
    val test: Boolean = false,
    var mediaController: MediaController? = null,
    val selectedEpisode: Episode? = null,
    val selectedPodcast: PodcastWithRelations? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val sponsorSectionStart: Long? = null,
    val sponsorSectionEnd: Long? = null,
    val isPreviewing: PreviewState = PreviewState.NONE,
    val sponsorSections: List<SponsorSection> = emptyList(),
    val currentNavEpisode: Episode? = null,
    val activeUpdateUrls: List<String> = emptyList(),
)

enum class PreviewState{
    NONE,
    PREVIEWING,
    FINISHED,
}