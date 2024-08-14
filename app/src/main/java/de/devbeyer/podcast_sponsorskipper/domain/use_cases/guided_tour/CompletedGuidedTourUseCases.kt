package de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour

data class CompletedGuidedTourUseCases(
    val getCompletedGuidedTourUseCase : GetCompletedGuidedTourUseCase,
    val setCompletedGuidedTourUseCase : SetCompletedGuidedTourUseCase,
    val resetCompletedGuidedTourUseCase: ResetCompletedGuidedTourUseCase,
)
