package de.devbeyer.podcast_sponsorskipper.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun Context.openLink(url: String) {
    Log.i("Link", url)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    startActivity(intent)
}