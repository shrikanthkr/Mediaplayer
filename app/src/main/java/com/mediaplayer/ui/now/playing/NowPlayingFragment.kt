package com.mediaplayer.ui.now.playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mediaplayer.app.R
import com.mediaplayer.app.ViewModelFactory
import com.mediaplayer.app.activities.home.HomeActivityViewModel
import com.mediaplayer.app.databinding.FragmentNowplayingBinding
import com.mediaplayer.app.di.components.FragmentComponent
import com.mediaplayer.app.models.PlayerState.Paused
import com.mediaplayer.app.models.PlayerState.Playing
import com.mediaplayer.app.utils.load
import com.mediaplayer.repository.albumArtPath
import com.mediaplayer.repository.formattedDuration
import com.mediaplayer.ui.BaseFragment
import javax.inject.Inject

class NowPlayingFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewBinding: FragmentNowplayingBinding
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private lateinit var viewModel: NowPlayingViewModel
    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeActivityViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(NowPlayingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentNowplayingBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.downArrow.setOnClickListener {
            homeActivityViewModel.updateState(BottomSheetBehavior.STATE_HIDDEN)
        }
        viewModel.currentSongLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                viewBinding.album.text = it.album
                viewBinding.songTitle.text = it.title
                viewBinding.artistTitle.text = it.artist
                viewBinding.albumImage.load(it.albumArtPath())
                viewBinding.playerSeekbar.max = it.duration.toInt()
            }
        })
        viewModel.playerStateLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Playing -> {
                    viewBinding.playerSeekbar.progress = it.progress.toInt()
                    viewBinding.playPause.setImageResource(R.drawable.ic_pause)
                    viewBinding.totalTime.text = it.song.duration.formattedDuration()
                    viewBinding.currentTime.text = it.progress.formattedDuration()
                }
                is Paused -> {
                    viewBinding.playPause.setImageResource(R.drawable.ic_play_arrow)
                }
            }
        })
        viewBinding.playerSeekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

        })

        viewBinding.playPause.setOnClickListener {
            viewModel.togglePlay()
        }
    }

}