package com.mediaplayer.repository

import android.content.ContentUris
import android.net.Uri


data class Song(val title: String,
                val artist: String,
                val album: String,
                val id: String,
                val displayName: String,
                val data: String,
                val duration: String,
                val album_art: String,
                val album_id: String,
                val playlist: String? = null) {
    companion object
}

fun Song.albumArtPath(): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), id.toLong())
}