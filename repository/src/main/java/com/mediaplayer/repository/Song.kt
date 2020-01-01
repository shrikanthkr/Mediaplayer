package com.mediaplayer.repository


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