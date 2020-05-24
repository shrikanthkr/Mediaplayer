package com.em.mediaplayer.app.di.components

import com.em.mediaplayer.app.di.scopes.FragmentScope
import com.em.mediaplayer.ui.albums.AlbumsFragment
import com.em.mediaplayer.ui.artist.ArtistsFragment
import com.em.mediaplayer.ui.now.playing.NowPlayingFragment
import com.em.mediaplayer.ui.search.SearchFragment
import com.em.mediaplayer.ui.songs.SongsFragment
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
@ExperimentalStdlibApi
@FragmentScope
@Subcomponent
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }

    fun inject(fragment: SongsFragment)
    fun inject(fragment: NowPlayingFragment)
    fun inject(fragment: AlbumsFragment)
    fun inject(artistsFragment: ArtistsFragment)
    fun inject(searchFragment: SearchFragment)
}