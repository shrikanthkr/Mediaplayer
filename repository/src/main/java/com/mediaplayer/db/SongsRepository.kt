package com.mediaplayer.db

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.mediaplayer.repository.Song
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongsRepository @Inject constructor(private val application: Application, private val ioDispatcher: CoroutineDispatcher) {

    private fun getMediaStoreCursor(): Cursor? {
        return application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, null, sort)
    }

    suspend fun getSongs() = withContext(ioDispatcher) {
        val songInfo = mutableListOf<Song>()
        val c = getMediaStoreCursor()
        c!!.moveToFirst()
        while (!c.isAfterLast) {
            val item = Song.build(c)
            songInfo.add(item)
            c.moveToNext()
        }
        c.close()
        songInfo
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
    private fun Song.Companion.build(c: Cursor): Song {
        return Song(
                c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID)),
                c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
                Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID))),
                c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)),
                c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
        )
    }
}