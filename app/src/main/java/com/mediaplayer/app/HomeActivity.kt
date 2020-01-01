package com.mediaplayer.app


import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mediaplayer.ui.AlbumsFragment
import com.mediaplayer.ui.ArtistsFragment
import com.mediaplayer.ui.PlaylistFragment
import com.mediaplayer.ui.home.HomeFragment


//https://dribbble.com/shots/6605936-Spotify-visual-concept-Sneak-peek/attachments
class HomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        if (savedInstanceState == null) {
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_home -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(), "${it.itemId}").commit()
                    }
                    R.id.action_album -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AlbumsFragment.newInstance(), "${it.itemId}").commit()
                    }
                    R.id.action_playlist -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PlaylistFragment.newInstance(), "${it.itemId}").commit()
                    }
                    R.id.action_artists -> {
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ArtistsFragment.newInstance(), "${it.itemId}").commit()
                    }
                }
                true
            }

            bottomNavigationView.selectedItemId = R.id.action_home
        }
    }
}
