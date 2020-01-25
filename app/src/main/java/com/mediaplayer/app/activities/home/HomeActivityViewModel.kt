package com.mediaplayer.app.activities.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mediaplayer.repository.Song

class HomeActivityViewModel() : ViewModel() {
    private val _playingFragmentState = MutableLiveData<Int>()
    val playingFragmentState = _playingFragmentState

    private val _currentSong = MutableLiveData<Song>()
    val currentSong = _currentSong

    fun updateState(@BottomSheetBehavior.State state: Int) {
        _playingFragmentState.value = state
    }

    fun setCurrentSong(song: Song) {
        _currentSong.value = song
    }

    companion object {
        const val TAG = "HomeActivityViewModel"
    }
}
