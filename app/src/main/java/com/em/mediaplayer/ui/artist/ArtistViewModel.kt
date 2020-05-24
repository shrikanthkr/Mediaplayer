package com.em.mediaplayer.ui.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.em.db.SongsRepository
import com.em.mediaplayer.app.di.scopes.FragmentScope
import com.em.mediaplayer.player.PlayerController
import com.em.repository.Artist
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@FragmentScope
class ArtistViewModel @Inject constructor(private val repository: SongsRepository, private val playerController: PlayerController) : ViewModel() {

    private val _artists = MutableLiveData<List<Artist>>()
    val artists = _artists

    init {
        viewModelScope.launch {
            _artists.value = repository.artists()
        }

    }

    @ExperimentalStdlibApi
    fun playArtist(artist: Artist) {
        playerController.clear()
        viewModelScope.launch {
            val songs = repository.songs(artist)
            playerController.playNow(songs.removeFirst())
            playerController.queueAll(songs)
        }
    }
}