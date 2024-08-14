package de.devbeyer.podcast_sponsorskipper.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.devbeyer.podcast_sponsorskipper.data.local.dao.PodcastDao
import de.devbeyer.podcast_sponsorskipper.ui.navigation.NavGraph
import de.devbeyer.podcast_sponsorskipper.ui.theme.PodcastSponsorSkipperTheme
import de.devbeyer.podcast_sponsorskipper.util.Constants
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

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
        createNotificationChannel()

        setContent {
            val startDestination = viewModel.startDestination

            PodcastSponsorSkipperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        startDestination = startDestination,
                        startWithNotificationPermission = {
                            Log.i("AAA", "startWithNotificationPermission ----")
                            startWithNotificationPermission()
                        }
                    )
                }
            }
        }
    }

    private fun startWithNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun createNotificationChannel() {
        val downloadChannel = NotificationChannel(
            Constants.DOWNLOAD_CHANNEL_ID,
            "Download Channel",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Channel for download notifications"
        }
        val updateChannel = NotificationChannel(
            Constants.UPDATE_CHANNEL_ID,
            "Update Channel",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Channel for update notifications"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(downloadChannel)
        manager.createNotificationChannel(updateChannel)
    }
}
