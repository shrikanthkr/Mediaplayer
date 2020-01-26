package com.mediaplayer.ui.customview

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.mediaplayer.app.R
import com.mediaplayer.app.utils.load
import findSuitableParent

class PlayerSnackBarContainer(parent: ViewGroup,
                              content: ViewGroup,
                              callback: com.google.android.material.snackbar.ContentViewCallback) : BaseTransientBottomBar<PlayerSnackBarContainer>(parent, content, callback) {


    private val albumArt: ImageView = content.findViewById(R.id.album_image)
    private val play: ImageView = content.findViewById(R.id.play_pause_image)
    private val titleView: TextView = content.findViewById(R.id.title)

    init {
        duration = Snackbar.LENGTH_INDEFINITE
    }

    fun setPlayIcon(@DrawableRes res: Int, playPauseClick: View.OnClickListener) {
        play.setImageResource(res)
        play.setOnClickListener(playPauseClick)
    }

    fun setTitle(title: String) {
        titleView.text = title
    }

    fun loadAlbumArt(path: Uri) {
        albumArt.load(path)
    }

    companion object {

        fun make(view: View): PlayerSnackBarContainer {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                    "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            val customView = LayoutInflater.from(view.context).inflate(
                    R.layout.snack_bar_player_view,
                    parent,
                    false
            ) as SnackBarPlayerView

            // We create and return our Snackbar
            return PlayerSnackBarContainer(parent, customView, customView)
        }

    }

}

