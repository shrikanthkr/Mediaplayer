package com.em.mediaplayer.app.activities.home


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.em.mediaplayer.app.R
import com.em.mediaplayer.app.ViewModelFactory
import com.em.mediaplayer.app.ViewPagerAdapter
import com.em.mediaplayer.app.activities.BaseActivity
import com.em.mediaplayer.app.activities.home.PermissionsHandler.Permission.DeniedPermission
import com.em.mediaplayer.app.activities.home.PermissionsHandler.Permission.Granted
import com.em.mediaplayer.app.behaviors.AppbarOffsetChangeListener
import com.em.mediaplayer.app.models.PlayerState.Completed
import com.em.mediaplayer.app.models.PlayerState.Playing
import com.em.mediaplayer.ui.customview.PlayerSnackBarContainer
import com.em.mediaplayer.ui.now.playing.NowPlayingFragment
import com.em.repository.formattedDuration
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


//https://material.io/resources/icons/?icon=pause&style=baseline
//https://dribbble.com/shots/6605936-Spotify-visual-concept-Sneak-peek/attachments
@ExperimentalStdlibApi
@FlowPreview
@ExperimentalCoroutinesApi
class HomeActivity : BaseActivity() {

    private lateinit var activityHome: CoordinatorLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var nowPlaying: NowPlayingFragment
    private lateinit var title: TextView
    private lateinit var pageIndicator: FloatingActionButton
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var appbarLayout: AppBarLayout
    private lateinit var snackBar: PlayerSnackBarContainer
    private lateinit var permissionsHandler: PermissionsHandler

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionsHandler = PermissionsHandler(this)
        lifecycle.addObserver(permissionsHandler)
        activityComponent.inject(this)

        setContentView(R.layout.activity_home)
        viewPager2 = findViewById(R.id.view_pager)
        activityHome = findViewById(R.id.activity_home)

        nowPlaying = supportFragmentManager.findFragmentById(R.id.now_playing) as NowPlayingFragment
        bottomSheetBehavior = BottomSheetBehavior.from(nowPlaying.requireView())
        title = findViewById(R.id.title)
        appbarLayout = findViewById(R.id.app_bar_layout)
        pageIndicator = findViewById(R.id.current_page_indicator)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeActivityViewModel::class.java)
        snackBar = PlayerSnackBarContainer.make(this.viewPager2)
        snackBar.view.setOnClickListener(snackBarClick)
        bottomSheetBehavior.peekHeight = 0
        bottomSheetBehavior.isHideable = true
        if (savedInstanceState == null) {
            viewModel.updateState(BottomSheetBehavior.STATE_HIDDEN)
        } else {
            bottomSheetBehavior.onRestoreInstanceState(activityHome, nowPlaying.requireView(), requireNotNull(savedInstanceState.getParcelable(BOTTOM_SHEET_STATE)))
        }
        permissionsHandler.permissionAvailable.observe(this, {
            when (it) {
                is Granted -> {
                    render()
                    alertDialog.dismiss()
                }
                is DeniedPermission -> {
                    alertDialog.show()
                }
            }
        })
        appbarLayout.addOnOffsetChangedListener((AppbarOffsetChangeListener(pageIndicator)))
        pageIndicator.setOnClickListener {
            viewModel.scrollTop(viewPager2.currentItem)
            if (viewPager2.currentItem != 0) {
                appbarLayout.setExpanded(true)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BOTTOM_SHEET_STATE, bottomSheetBehavior.onSaveInstanceState(activityHome, nowPlaying.requireView()))
    }

    private fun render() {
        viewPager2.adapter = ViewPagerAdapter(this)

        viewModel.playingFragmentState.observe(this, Observer {
            bottomSheetBehavior.state = it
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) {
                    appbarLayout.setExpanded(false)
                    appbarLayout.postDelayed({
                        appbarLayout.visibility = View.GONE
                    }, 300)
                } else {
                    appbarLayout.visibility = View.VISIBLE
                }
                title.text = getTitleText(position)
                pageIndicator.setImageResource(getIcon(position))
            }
        })
        viewPager2.setCurrentItem(1, false)

        viewModel.playerState.observe(this, Observer {
            when (it) {
                is Playing -> {
                    snackBar.setPlayIcon(R.drawable.ic_pause, playPauseClick)
                    snackBar.setDuration(it.progress.formattedDuration())
                }
                is Completed -> {
                    snackBar.setPlayIcon(R.drawable.ic_play_arrow, playPauseClick)
                }
                else -> {
                    snackBar.setPlayIcon(R.drawable.ic_play_arrow, playPauseClick)
                }
            }
        })

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.d(TAG, "Bottom Sheet state: $newState")
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> snackBar.show()
                    BottomSheetBehavior.STATE_EXPANDED -> snackBar.dismiss()
                    else -> snackBar.show()
                }
            }

        })

        viewModel.currentSong.observe(this, Observer {
            snackBar.show()
            snackBar.setTitle(it.title)
            snackBar.loadAlbumArt(it.albumArtPath)
        })

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

    private val playPauseClick = View.OnClickListener {
        viewModel.togglePlay()
    }

    private fun getTitleText(position: Int): String {
        return when (position) {
            0 -> getString(R.string.search_title)
            1 -> getString(R.string.songs)
            2 -> getString(R.string.albums)
            3 -> getString(R.string.artists)
            else -> getString(R.string.songs)
        }
    }

    private fun getIcon(position: Int): Int {
        return when (position) {
            0 -> R.drawable.ic_search
            1 -> R.drawable.ic_music_note
            2 -> R.drawable.ic_album
            3 -> R.drawable.ic_atrist
            else -> R.drawable.ic_music_note
        }
    }

    private val alertDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle(R.string.permision_request_title)
                .setMessage(R.string.permision_request_description)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", packageName, null)))
                }
                .create()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsHandler.handleResults(requestCode, grantResults)
    }


    companion object {
        const val BOTTOM_SHEET_STATE = "BOTTOM_SHEET_STATE"
        const val TAG = "HomeActivity"
    }
}

