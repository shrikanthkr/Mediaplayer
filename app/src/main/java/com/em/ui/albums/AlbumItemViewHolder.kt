package com.em.ui.songs

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.em.app.R
import com.em.app.utils.load

class AlbumItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    private var title: TextView = itemView.findViewById(R.id.title)
    private var albumArt: ImageView = itemView.findViewById(R.id.album_art)

    fun setTitle(title: String) {
        this.title.text = title
    }

    fun setAlbumArt(uri: Uri) {
        albumArt.load(uri)
    }

}