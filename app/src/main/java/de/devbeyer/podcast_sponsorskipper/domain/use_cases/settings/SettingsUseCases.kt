package de.devbeyer.podcast_sponsorskipper.domain.use_cases.settings

data class SettingsUseCases (
    val getSettingsUseCase: GetSettingsUseCase,
    val setBooleanSettingUseCase: SetBooleanSettingUseCase,
    val setIntSettingUseCase: SetIntSettingUseCase,
)