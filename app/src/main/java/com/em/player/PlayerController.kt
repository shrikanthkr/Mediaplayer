package com.em.player

import android.util.Log
import com.em.app.models.PlayerState
import com.em.app.models.PlayerState.*
import com.em.repository.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@Singleton
class PlayerController @Inject constructor(private val playerAdapter: PlayerAdapter, scope: CoroutineScope) {

    private val _channel = ConflatedBroadcastChannel<PlayerState>()

    @FlowPreview
    val channel = _channel.asFlow()

    private var _currentSong: Song? = null

    init {
        playerAdapter.addListener(object : PlayerListener {
            override fun onStart() {
                scope.launch {
                    _channel.send(Started(requireNotNull(_currentSong)))
                }
                Log.d(TAG, "Start")
            }

            override fun onPause() {
                Log.d(TAG, "Pause")
                val previousState = requireNotNull(_channel.value) as Playing
                scope.launch {
                    _channel.send(Paused(previousState.song, previousState.progress))
                }
            }

            override fun onProgress(progress: Long) {
                when (_channel.value) {
                    is Playing -> {
                        val previousState = requireNotNull(_channel.value) as Playing
                        scope.launch {
                            _channel.send(Playing(previousState.song, progress))
                        }
                    }
                    is Paused -> {
                        val previousState = requireNotNull(_channel.value) as Paused
                        scope.launch {
                            _channel.send(Playing(previousState.song, progress))
                        }
                    }
                    else -> {
                        val previousState = requireNotNull(_channel.value) as Started
                        scope.launch {
                            _channel.send(Playing(previousState.song, progress))
                        }
                    }
                }
            }

            override fun onEnd() {
                if (_channel.value is Playing) {
                    val previousState = requireNotNull(_channel.value) as Playing
                    scope.launch {
                        _channel.send(Completed(previousState.song))
                    }
                } else {
                    val previousState = requireNotNull(_channel.value) as Paused
                    scope.launch {
                        _channel.send(Completed(previousState.song))
                    }
                }
            }

            override fun onError() {
                Log.d(TAG, "Erred")
                scope.launch {
                    _channel.send(Erred)
                }
            }

        })
        scope.launch {
            _channel.send(Idle)
        }
    }

    fun play(song: Song) {
        _currentSong = song
        playerAdapter.play(song.uri)
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


    companion object {
        const val TAG = "PlayerController"
    }
}