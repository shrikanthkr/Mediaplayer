package com.em.ui.songs

import android.net.Uri
import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.em.app.databinding.ViewholderHomeItemBinding
import com.em.app.utils.load

class SongsItemViewHolder(private val itemBinding: ViewholderHomeItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun setTitle(title: String) {
        itemBinding.title.text = title
    }

    fun setSubTitle(title: String) {
        itemBinding.subTitle.text = title
    }

    fun setAlbumArt(uri: Uri) {
        itemBinding.albumArt.load(uri)
    }

    fun setQueueClick(@NonNull l: View.OnClickListener) {
        itemBinding.queue.setOnClickListener(l)
    }


}