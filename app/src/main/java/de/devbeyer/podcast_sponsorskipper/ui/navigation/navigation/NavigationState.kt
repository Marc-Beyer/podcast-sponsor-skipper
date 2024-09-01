package de.devbeyer.podcast_sponsorskipper.ui.navigation.navigation

import androidx.media3.session.MediaController
import de.devbeyer.podcast_sponsorskipper.domain.models.Settings
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Episode
import de.devbeyer.podcast_sponsorskipper.domain.models.db.Podcast
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
    val activeUpdateUrls: List<String> = emptyList(),
    val settings: Settings = Settings(),
    val selectedPodcastForUnsubscribe: Podcast? = null,
    val navigateUp: () -> Unit = {},
    val isSpeedOpen: Boolean = false,

    val currentNavEpisode: Episode? = null,
    val currentNavPodcast: PodcastWithRelations? = null,
    val currentPlaybackSpeed: Float = 1.0f,
)

enum class PreviewState{
    NONE,
    PREVIEWING,
    FINISHED,
}