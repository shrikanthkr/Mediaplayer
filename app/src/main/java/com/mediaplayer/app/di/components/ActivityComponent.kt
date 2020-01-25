package com.mediaplayer.app.di.components

import com.mediaplayer.app.activities.home.HomeActivity
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(activity: HomeActivity)
}