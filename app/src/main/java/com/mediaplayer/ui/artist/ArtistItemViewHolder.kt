package com.mediaplayer.ui.artist

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mediaplayer.app.R

class ArtistItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    private var title: TextView = itemView.findViewById(R.id.title)
    private var albumArt: ImageView = itemView.findViewById(R.id.album_art)

    fun setTitle(title: String) {
        this.title.text = title
    }

}