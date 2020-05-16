package com.mediaplayer.repository

import android.content.ContentUris
import android.net.Uri

data class Album(val id: String, val title: String, val count: Int) {
    val albumArtPath: Uri by lazy {
        ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), id.toLong())
    }
}