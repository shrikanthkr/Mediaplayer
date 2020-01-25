package com.mediaplayer.repository

import android.content.ContentUris
import android.net.Uri


data class Song(val title: String,
                val artist: String,
                val album: String,
                val id: String,
                val displayName: String,
                val data: String,
                val duration: Long,
                val album_art: String,
                val album_id: String,
                val playlist: String? = null) {
    companion object
}

fun Song.albumArtPath(): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), id.toLong())
}


fun Long.formattedDuration(): String {
    val sec = this / 1000
    return (sec / 60).toString().padStart(2, '0') + ":" + (sec % 60).toString().padStart(2, '0')
}