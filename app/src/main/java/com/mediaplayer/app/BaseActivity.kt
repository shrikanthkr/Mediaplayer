package com.mediaplayer.app


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mediaplayer.app.di.components.AppComponent


//https://dribbble.com/shots/6605936-Spotify-visual-concept-Sneak-peek/attachments
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val appComponent: AppComponent
        get() = (applicationContext as MediaApplication).appComponent
}
