package com.em.mediaplayer.ui.now.playing

import androidx.recyclerview.widget.RecyclerView
import com.em.mediaplayer.app.databinding.ViewholderSongSmallItemBinding

class SongsItemSmallViewHolder(private val itemBinding: ViewholderSongSmallItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun setTitle(title: String) {
        itemBinding.title.text = title
    }


}