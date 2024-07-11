package de.devbeyer.podcast_sponsorskipper.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.devbeyer.podcast_sponsorskipper.domain.use_cases.guided_tour.CompletedGuidedTourUseCases
import de.devbeyer.podcast_sponsorskipper.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val completedGuidedTourUseCases: CompletedGuidedTourUseCases
) : ViewModel() {
    var isSplashScreenActive by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf(Screen.Start.route)
        private set

    init{
        completedGuidedTourUseCases.getCompletedGuidedTourUseCase().onEach {
            if(it){
                startDestination = Screen.Main.route
            }else{
                startDestination = Screen.Start.route
            }
            delay(300)
            isSplashScreenActive = false
        }.launchIn(viewModelScope)
    }
}