package com.em.app.di.components

import com.em.app.di.scopes.FragmentScope
import com.em.ui.albums.AlbumsFragment
import com.em.ui.artist.ArtistsFragment
import com.em.ui.now.playing.NowPlayingFragment
import com.em.ui.songs.SongsFragment
import dagger.Subcomponent


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
}