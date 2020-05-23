package com.em.ui.now.playing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.em.app.R
import com.em.app.ViewModelFactory
import com.em.app.activities.home.HomeActivityViewModel
import com.em.app.databinding.FragmentNowplayingBinding
import com.em.app.di.components.FragmentComponent
import com.em.app.models.PlayerState.Paused
import com.em.app.models.PlayerState.Playing
import com.em.app.utils.load
import com.em.repository.formattedDuration
import com.em.ui.BaseFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
                viewBinding.albumImage.load(it.albumArtPath)
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
        viewBinding.next.setOnClickListener {
            viewModel.next()
        }
        viewBinding.previous.setOnClickListener {
            viewModel.previous()
        }

    }

}