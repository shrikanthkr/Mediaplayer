package com.em.mediaplayer.app.utils

import android.net.Uri
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.em.mediaplayer.app.R
import com.em.mediaplayer.ui.glide.AudioCover
import com.em.repository.Song

fun ImageView.load(url: Uri) {
    val errorDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_album, null)
    val thumbnail = Glide.with(context).load(errorDrawable)
    errorDrawable?.setTint(context.getColorCompat(R.color.gray))
    Glide.with(this)
            .load(url)
            .thumbnail(thumbnail)
            .error(errorDrawable)
            .into(this)
}


fun ImageView.loadAlbum(uri: Uri) {
    val errorDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_album, null)
    val thumbnail = Glide.with(context).load(errorDrawable)
    errorDrawable?.setTint(context.getColorCompat(R.color.gray))
    Glide.with(this)
            .load(AudioCover(uri))
            .thumbnail(thumbnail)
            .error(errorDrawable)
            .into(this)
}



