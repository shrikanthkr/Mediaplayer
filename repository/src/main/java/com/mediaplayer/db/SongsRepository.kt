package com.mediaplayer.db

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.mediaplayer.repository.Song
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SongsRepository @Inject constructor(private val application: Application) {

    private fun getMediaStoreCursor(additional: String?, limit: String): Cursor? {
        var mutatedLimit: String? = limit
        val selection = StringBuilder()
        val sortOrder = "LOWER(" + MediaStore.Audio.Media.TITLE + ")"
        selection.append("( " + MediaStore.Audio.Media.IS_MUSIC + " != 0 AND LOWER("
                + MediaStore.Audio.Media.DISPLAY_NAME
                + ") NOT LIKE  LOWER('%.wma') ")
        if (additional != null && additional.length > 0) {
            selection.append("AND $additional")
        }
        selection.append(" )")
        mutatedLimit = if (mutatedLimit != null && mutatedLimit.length > 0) {
            " LIMIT $mutatedLimit"
        } else {
            ""
        }
        val projection = arrayOf(MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM_ID)
        return application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection.toString(), null, sortOrder + mutatedLimit)
    }

    fun getSongs(search: String? = null): List<Song> {
        val songInfo = mutableListOf<Song>()
        var query: String? = null
        if (search?.isNotEmpty() == true) {
            query = ("LOWER("
                    + MediaStore.Audio.Media.TITLE + ") LIKE LOWER('" + search + "%') ")
        }
        val c = getMediaStoreCursor(query, "")
        c!!.moveToFirst()
        while (!c.isAfterLast) {
            val item = Song.build(c)
            songInfo.add(item)
            c.moveToNext()
        }
        c.close()
        return songInfo
    }


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