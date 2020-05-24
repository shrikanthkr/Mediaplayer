package com.em.mediaplayer.player

import android.util.Log
import com.em.db.SongsRepository
import com.em.mediaplayer.app.models.PlayerState
import com.em.mediaplayer.app.models.PlayerState.*
import com.em.repository.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class PlayerController @Inject constructor(private var playerAdapter: PlayerAdapter, private val scope: CoroutineScope, private val respository: SongsRepository) {

    private val _playerState = ConflatedBroadcastChannel<PlayerState>()

    @FlowPreview
    val playerState = _playerState.asFlow()

    private val _currentSongChannel = ConflatedBroadcastChannel<Song>()
    val currentSongChannel = _currentSongChannel.asFlow()

    private var externalActor: Int = 0

    private val defaultAdapter: PlayerAdapter = playerAdapter

    init {

        playerAdapter.addListener(object : PlayerListener {
            override fun onStart() {
                dispatch(Started(requireNotNull(_currentSongChannel.value)))
                Log.d(TAG, "Start")
            }

            override fun onPause() {
                Log.d(TAG, "Pause")
                val previousState = requireNotNull(_playerState.value) as Playing
                dispatch(Paused(previousState.song, previousState.progress))
            }

            override fun onProgress(progress: Long) {
                when (_playerState.value) {
                    is Playing -> {
                        val previousState = requireNotNull(_playerState.value) as Playing
                        dispatch(Playing(previousState.song, progress))
                    }
                    is Paused -> {
                        val previousState = requireNotNull(_playerState.value) as Paused
                        dispatch(Playing(previousState.song, progress))
                    }
                    else -> {
                        val previousState = requireNotNull(_playerState.value) as Started
                        dispatch(Playing(previousState.song, progress))
                    }
                }
            }

            override fun onEnd() {
                if (_playerState.value is Playing) {
                    val previousState = requireNotNull(_playerState.value) as Playing
                    dispatch(Completed(previousState.song))
                } else {
                    val previousState = requireNotNull(_playerState.value) as Paused
                    dispatch(Completed(previousState.song))
                }
                next()
            }

            override fun onError() {
                Log.d(TAG, "Erred")
                dispatch(Erred)
            }

        })
        dispatch(Idle)
    }


    fun pause() {
        playerAdapter.pause()
    }

    fun resume() {
        playerAdapter.resume()
    }

    fun seek(position: Long) {
        playerAdapter.seek(position)
    }

    fun next() {
        respository.next()?.apply {
            play(this)
        }
    }

    fun playNow(song: Song) {
        respository.addAtStart(song)
        play(song)
    }

    fun previous() {
        respository.previous()?.apply {
            play(this)
        }
    }

    fun queue(song: Song) {
        respository.queue(song)
    }

    fun queueAll(songs: List<Song>) {
        respository.queueAll(songs)
    }

    fun clear() {
        pause()
        respository.clear()
    }

    fun pauseBy(accessorId: Int) {
        if (_playerState.value is Playing) {
            pause()
            externalActor = externalActor xor accessorId
        }
    }

    fun resumeBy(accessorId: Int) {
        if (externalActor xor accessorId == 0) {
            resume()
            externalActor = externalActor xor accessorId
        }
    }

    fun switchAdapter(adapter: PlayerAdapter) {
        playerAdapter.clear()
        playerAdapter = adapter
        play(_currentSongChannel.value)
    }

    fun switchToDefaultAdapter() {
        switchAdapter(defaultAdapter)
    }


    private fun play(song: Song) {
        scope.launch {
            _currentSongChannel.send(song)
        }
        playerAdapter.play(song)
    }

    private fun dispatch(state: PlayerState) {
        scope.launch {
            _playerState.send(state)
        }
    }

    companion object {
        const val TAG = "PlayerController"
    }
}