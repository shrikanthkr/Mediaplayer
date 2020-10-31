package com.em.mediaplayer.app.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.em.mediaplayer.app.MediaApplication
import com.em.mediaplayer.app.di.components.ActivityComponent
import com.em.mediaplayer.app.di.components.AppComponent


//https://dribbble.com/shots/6605936-Spotify-visual-concept-Sneak-peek/attachments


open class BaseActivity : AppCompatActivity() {
    lateinit var activityComponent: ActivityComponent
    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = appComponent.activityComponent().create()
        super.onCreate(savedInstanceState)
    }

    val appComponent: AppComponent
        get() = (applicationContext as MediaApplication).appComponent
}
