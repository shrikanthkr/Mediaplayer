package com.em.db

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.em.repository.Album
import com.em.repository.Artist
import com.em.repository.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongsRepository @Inject constructor(private val application: Application, private val ioDispatcher: CoroutineDispatcher) {

    private fun songsCursor(): Cursor? {
        return application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, null, sort)
    }

    private fun albumsCursor(): Cursor? {
        val projection = arrayOf(MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS)

        return application.contentResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Audio.Albums.ALBUM + " ASC")
    }

    private fun artistCursor(): Cursor? {
        val projection = arrayOf(MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS)

        return application.contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " ASC")

    }

    suspend fun all() = withContext(ioDispatcher) {
        val songInfo = mutableListOf<Song>()
        val c = songsCursor()
        c?.apply {
            this.moveToFirst()
            while (!this.isAfterLast) {
                val item = this.toSong()
                songInfo.add(item)
                this.moveToNext()
            }
            c.close()
        }
        songInfo
    }

    suspend fun albums() = withContext(ioDispatcher) {
        val albums = mutableListOf<Album>()
        val c = albumsCursor()
        c?.apply {
            this.moveToFirst()
            while (!this.isAfterLast) {
                val item = c.toAlbum()
                albums.add(item)
                this.moveToNext()
            }
            c.close()
        }
        albums
    }

    suspend fun artists() = withContext(ioDispatcher) {
        val artists = mutableListOf<Artist>()
        val c = artistCursor()
        c?.apply {
            this.moveToFirst()
            while (!this.isAfterLast) {
                val item = c.toArtist()
                artists.add(item)
                this.moveToNext()
            }
            c.close()
        }
        artists
    }


    companion object {
        const val sort = "LOWER(${MediaStore.Audio.Media.TITLE})"
        const val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND LOWER(${MediaStore.Audio.Media.DISPLAY_NAME})  NOT LIKE  LOWER('%.wma') "
        @SuppressLint("InlinedApi")
        val projection = arrayOf(MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID)
    }

    @SuppressLint("InlinedApi")
    private fun Cursor.toSong(): Song {
        return Song(
                getString(getColumnIndex(MediaStore.Audio.Media.TITLE)),
                getString(getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                getString(getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                getString(getColumnIndex(MediaStore.Audio.Media._ID)),
                getString(getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getString(getColumnIndex(MediaStore.Audio.Media._ID))),
                getLong(getColumnIndex(MediaStore.Audio.Media.DURATION)),
                getString(getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)),
                getString(getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
        )
    }

    private fun Cursor.toAlbum(): Album {
        return Album(
                getString(getColumnIndex(MediaStore.Audio.Media._ID)),
                getString(getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                getInt(getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
        )
    }

    private fun Cursor.toArtist(): Artist {
        return Artist(
                getString(getColumnIndex(MediaStore.Audio.Artists._ID)),
                getString(getColumnIndex(MediaStore.Audio.Artists.ARTIST)),
                getInt(getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS))
        )
    }


}