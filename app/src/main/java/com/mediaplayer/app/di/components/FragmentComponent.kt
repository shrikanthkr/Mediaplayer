package com.mediaplayer.app.di.components

import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.ui.albums.AlbumsFragment
import com.mediaplayer.ui.now.playing.NowPlayingFragment
import com.mediaplayer.ui.songs.SongsFragment
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
}