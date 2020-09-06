package com.em.db

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.em.repository.Album
import com.em.repository.Artist
import com.em.repository.Song
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@FlowPreview
@ExperimentalCoroutinesApi
class SongsRepository @Inject constructor(private val application: Application, private val ioDispatcher: CoroutineDispatcher, private val scope: CoroutineScope) {

    private val _queue = MutableStateFlow<MutableList<Song>>(mutableListOf())
    val queue = _queue.filterNotNull<List<Song>>()

    private var currentIndex = -1

    private fun songsCursor(selection: String? = null): Cursor? {
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
        val c = songsCursor(selection)
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

    suspend fun songs(album: Album) = withContext(ioDispatcher) {
        val songsInfo = mutableListOf<Song>()
        val query = MediaStore.Audio.Media.ALBUM_ID + " = '" + album.id + "'"
        val c = songsCursor(query)
        c?.apply {
            this.moveToFirst()
            while (!this.isAfterLast) {
                val item = this.toSong()
                songsInfo.add(item)
                this.moveToNext()
            }
            c.close()
        }
        songsInfo
    }

    suspend fun songs(artist: Artist) = withContext(ioDispatcher) {
        val songsInfo = mutableListOf<Song>()
        val query = "$selection AND ${MediaStore.Audio.Media.ARTIST_ID}  = '${artist.id}'"
        val c = songsCursor(query)
        c?.apply {
            this.moveToFirst()
            while (!this.isAfterLast) {
                val item = this.toSong()
                songsInfo.add(item)
                this.moveToNext()
            }
            c.close()
        }
        songsInfo
    }

    suspend fun search(searchString: String) = withContext(ioDispatcher) {
        val songsInfo = mutableListOf<Song>()
        val query = "$selection AND LOWER(${MediaStore.Audio.Media.TITLE} ) LIKE LOWER('$searchString%') "
        val c = songsCursor(query)
        c?.apply {
            this.moveToFirst()
            while (!this.isAfterLast) {
                val item = this.toSong()
                songsInfo.add(item)
                this.moveToNext()
            }
            c.close()
        }
        songsInfo
    }


    fun next(): Song? {
        return if (_queue.value.size - 1 > currentIndex) {
            currentIndex += 1
            _queue.value[currentIndex]
        } else {
            null
        }
    }


    fun previous(): Song? {
        return if (currentIndex >= 1) {
            currentIndex -= 1
            _queue.value[currentIndex]
        } else {
            null
        }
    }


    fun queue(song: Song) = scope.launch {
        val currentSongs = _queue.value.toMutableList()
        currentSongs.add(song)
        scope.launch {
            _queue.value = currentSongs
        }
    }


    fun queueAll(songs: List<Song>) {
        val currentSongs = _queue.value.toMutableList()
        currentSongs.addAll(songs)
        scope.launch {
            _queue.value = currentSongs
        }
    }


    fun addAtStart(song: Song) = scope.launch {

        val songs = _queue.value.toMutableList()
        val songExpectedIndex = songs.indexOf(song)
        currentIndex = if (songExpectedIndex != -1) {
            songExpectedIndex
        } else {
            songs.add(0, song)
            scope.launch {
                _queue.value = songs
            }
            0
        }
    }


    fun clear() {
        scope.launch {
            _queue.value = mutableListOf()
        }
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