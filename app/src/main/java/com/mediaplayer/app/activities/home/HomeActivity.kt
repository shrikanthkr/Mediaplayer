package com.mediaplayer.app.activities.home


import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mediaplayer.app.R
import com.mediaplayer.app.ViewModelFactory
import com.mediaplayer.app.ViewPagerAdapter
import com.mediaplayer.app.activities.BaseActivity
import com.mediaplayer.app.models.PlayerState.Playing
import com.mediaplayer.app.utils.getTintedDrawable
import com.mediaplayer.ui.now.playing.NowPlayingFragment
import java.util.*
import javax.inject.Inject


//https://material.io/resources/icons/?icon=pause&style=baseline
//https://dribbble.com/shots/6605936-Spotify-visual-concept-Sneak-peek/attachments
class HomeActivity : BaseActivity() {
    private val stack = Stack<Int>()
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var title: TextView
    private lateinit var nowPlaying: NowPlayingFragment
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var snackBar: Snackbar
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        setContentView(R.layout.activity_home)
        viewPager2 = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        tabs.isTabIndicatorFullWidth = false
        title = findViewById(R.id.title)
        viewPager2.adapter = ViewPagerAdapter(this)
        nowPlaying = supportFragmentManager.findFragmentById(R.id.now_playing) as NowPlayingFragment
        bottomSheetBehavior = BottomSheetBehavior.from(nowPlaying.requireView())
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeActivityViewModel::class.java)
        if (savedInstanceState == null) {
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.peekHeight = 0
            viewModel.updateState(BottomSheetBehavior.STATE_HIDDEN)
        }
        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = getTitleText(position)
        }.attach()
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                title.text = getTitleText(position)
            }
        })

        viewModel.playingFragmentState.observe(this, Observer {
            bottomSheetBehavior.state = it
        })

        viewModel.playerStateLiveData.observe(this, Observer {
            when (it) {
                is Playing -> {
                    snackBar.setAction(playImageSpan, snackBarClick)
                }
                else -> {
                    snackBar.setAction(pauseImageSpan, snackBarClick)
                }
            }
        })

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> snackBar.show()
                    BottomSheetBehavior.STATE_EXPANDED -> snackBar.dismiss()
                    else -> snackBar.show()
                }
            }

        })

        viewModel.currentSong.observe(this, Observer {
            snackBar.show()
            snackBar.setText(it.title)
        })

        snackBar = Snackbar.make(this.viewPager2,
                R.string.album_image_description,
                Snackbar.LENGTH_INDEFINITE)


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
        when {
            bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            }
            supportFragmentManager.backStackEntryCount <= 1 -> finish()
            else -> {
                supportFragmentManager.popBackStack()
            }
        }
    }

    private val snackBarClick = View.OnClickListener {
        viewModel.updateState(BottomSheetBehavior.STATE_EXPANDED)
    }
    private val playImageSpan by lazy {
        val drawable = this.getTintedDrawable(R.drawable.ic_pause, R.attr.colorOnPrimary)
        val builder = SpannableStringBuilder().append("span")
        builder.setSpan(ImageSpan(drawable), 0, builder.length, 0)
        builder
    }
    private val pauseImageSpan by lazy {
        val drawable = this.getTintedDrawable(R.drawable.ic_play_arrow, R.attr.colorOnPrimary)
        val builder = SpannableStringBuilder().append("span")
        builder.setSpan(ImageSpan(drawable), 0, builder.length, 0)
        builder
    }

}

