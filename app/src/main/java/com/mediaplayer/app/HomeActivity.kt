package com.mediaplayer.app


import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*


//https://dribbble.com/shots/6605936-Spotify-visual-concept-Sneak-peek/attachments
class HomeActivity : BaseActivity() {
    private val stack = Stack<Int>()
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var title: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        viewPager2 = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        title = findViewById(R.id.title)
        viewPager2.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = getTitleText(position)
        }.attach()
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                title.text = getTitleText(position)
            }
        })

    }

    private fun getTitleText(position: Int): String {
        return when (position) {
            0 -> {
                getString(R.string.songs)
            }
            1 -> {
                getString(R.string.albums)
            }
            2 -> {
                getString(R.string.playlist)
            }
            3 -> {
                getString(R.string.artists)
            }
            else -> {
                getString(R.string.songs)
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount <= 1)
            finish()
        else {
            supportFragmentManager.popBackStack()
        }
    }

}
