package de.devbeyer.podcast_sponsorskipper.ui.tourguide

sealed class TourGuideEvent {
    object completedTourGuide: TourGuideEvent()
}