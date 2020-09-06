package com.em.mediaplayer.player

import android.util.Log
import com.em.db.SongsRepository
import com.em.mediaplayer.app.models.PlayerState
import com.em.mediaplayer.app.models.PlayerState.*
import com.em.mediaplayer.player.adapters.NoOpAdapter
import com.em.mediaplayer.player.adapters.PlayerAdapter
import com.em.repository.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@FlowPreview
@ExperimentalCoroutinesApi
@Singleton
class PlayerController @Inject constructor(private val scope: CoroutineScope, private val repository: SongsRepository) {

    var playerAdapter: PlayerAdapter = NoOpAdapter()
    private val _playerState = ConflatedBroadcastChannel<PlayerState>()

    @FlowPreview
    val playerState = _playerState.asFlow()

    private val _currentSongChannel = MutableStateFlow<Song?>(null)
    val currentSongChannel = _currentSongChannel.filterNotNull()

    private var externalActor: Int = 0

    private val playerListener = object : PlayerListener {
        override fun onStart() {
            dispatch(Started(requireNotNull(_currentSongChannel.value)))
            Log.d(TAG, "Start")
        }

        override fun onLoading() {
            Log.d(TAG, "Loading")
            dispatch(Loading)
        }

        override fun onPause() {
            Log.d(TAG, "Pause")
            when (val currentState = _playerState.value) {
                is Playing -> dispatch(Paused(currentState.song, currentState.progress))
                is Loading -> {
                    //ignore
                }
            }

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
            Log.d(TAG, "END")
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

    }

    init {
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
        repository.next()?.apply {
            play(this)
        }
    }

    fun playNow(song: Song) {
        repository.addAtStart(song)
        play(song)
    }

    fun previous() {
        repository.previous()?.apply {
            play(this)
        }
    }

    fun queue(song: Song) {
        repository.queue(song)
    }

    fun queueAll(songs: List<Song>) {
        repository.queueAll(songs)
    }

    fun clear() {
        pause()
        repository.clear()
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
        val currentPlayerState = _playerState.value
        //clear old adapter
        playerAdapter.clear()
        playerAdapter.removeListener(playerListener)

        //set new adapter
        playerAdapter = adapter
        playerAdapter.addListener(playerListener)

        //based on state play
        if (currentPlayerState is Playing) {
            play(requireNotNull(_currentSongChannel.value))
            seek(currentPlayerState.progress)
        }
    }


    private fun play(song: Song) {
        scope.launch {
            _currentSongChannel.value = song
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