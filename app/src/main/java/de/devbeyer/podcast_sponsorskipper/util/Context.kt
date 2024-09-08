package de.devbeyer.podcast_sponsorskipper.util

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri

fun Context.openLink(url: String, addFlags: Boolean = false) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        if (addFlags) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    startActivity(intent)
}

fun Context.isWifiOrNotMetered(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    val isMetered = connectivityManager.isActiveNetworkMetered
    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && !isMetered

}