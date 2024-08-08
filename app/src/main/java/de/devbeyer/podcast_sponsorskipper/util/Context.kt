package de.devbeyer.podcast_sponsorskipper.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun Context.openLink(url: String, addFlags: Boolean = false) {
    Log.i("Link", url)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        if(addFlags) {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    startActivity(intent)
}