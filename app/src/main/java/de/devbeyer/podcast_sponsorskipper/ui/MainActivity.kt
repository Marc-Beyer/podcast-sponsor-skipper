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
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.domain.models.Podcast
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavGraph
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var podcastDao: PodcastDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isSplashScreenActive
            }
        }

        actionBar?.hide()

        lifecycleScope.launch {
            podcastDao.insert(
                podcast = Podcast(
                    id = 1,
                    url = "https://changelog.com/jsparty/feed",
                    title = "JS Party: JavaScript, CSS, Web Development",
                    description = "Your weekly celebration of JavaScript and the web. Panelists include Jerod Santo, Feross Aboukhadijeh, Kevin Ball, Amelia Wattenberger, Nick Nisi, Divya Sasidharan, Mikeal Rogers, Chris Hiller, and Amal Hussein. Topics discussed include the web platform (Chrome, Safari, Edge, Firefox, Brave, etc), front-end frameworks (React, Solid, Svelte, Vue, Angular, etc), JavaScript and TypeScript runtimes (Node, Deno, Bun), web animation, SVG, robotics, IoT, and much more. If JavaScript and/or the web touch your life, this show’s for you. Some people search for JSParty and can’t find the show, so now the string JSParty is in our description too.",
                    link = "https://changelog.com/jsparty",
                    language = "en-us",
                    imageUrl = "https://cdn.changelog.com/uploads/covers/js-party-original.png?v=63725770332",
                    explicit = false,
                    locked = false,
                    complete = false,
                    lastUpdate = "String",
                    nrOdEpisodes = 42,
                    copyright = "All rights reserved",
                    author = "Changelog Media",
                    fundingText = "Support our work by joining Changelog++",
                    fundingUrl = "https://changelog.com/++",
                ),
            )
        }

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
