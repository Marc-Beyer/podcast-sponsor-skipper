package de.devbeyer.podcast_sponsorskipper.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavGraph
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen().setKeepOnScreenCondition{
        //    viewModel.isSplashScreenActive
        //}
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isSplashScreenActive
            }
        }
        //supportActionBar?.hide()
        actionBar?.hide()

        setContent {
            val startDestination = viewModel.startDestination

            PodcastSponsorSkipperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(startDestination = startDestination)
                }
            }
        }
    }
}
