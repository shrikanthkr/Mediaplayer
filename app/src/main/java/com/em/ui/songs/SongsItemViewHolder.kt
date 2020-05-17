package com.em.ui.songs

import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.em.app.R
import com.em.app.utils.load

class SongsItemViewHolder(itemView: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    private var title: TextView = itemView.findViewById(R.id.title)
    private var subTitle: TextView = itemView.findViewById(R.id.sub_title)
    private var albumArt: ImageView = itemView.findViewById(R.id.album_art)

    fun setTitle(title: String) {
        this.title.text = title
    }

    fun setSubTitle(title: String) {
        this.subTitle.text = title
    }

    fun setAlbumArt(uri: Uri) {
        albumArt.load(uri)
    }

}