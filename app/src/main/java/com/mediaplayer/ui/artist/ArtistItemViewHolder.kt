package com.mediaplayer.ui.songs

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mediaplayer.app.R
import com.mediaplayer.app.utils.load

class ArtistItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    private var title: TextView = itemView.findViewById(R.id.title)
    private var albumArt: ImageView = itemView.findViewById(R.id.album_art)

    fun setTitle(title: String) {
        this.title.text = title
    }

    fun setAlbumArt(uri: Uri) {
        albumArt.load(uri)
    }

}