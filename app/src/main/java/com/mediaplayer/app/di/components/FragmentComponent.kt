package com.mediaplayer.app.di.components

import com.mediaplayer.app.di.scopes.FragmentScope
import com.mediaplayer.ui.home.HomeFragment
import dagger.Subcomponent


@FragmentScope
@Subcomponent
interface FragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }

    fun inject(fragment: HomeFragment)
}