package com.em.mediaplayer.app.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.em.mediaplayer.app.R

fun ImageView.load(url: Uri) {
    val errorDrawable = resources.getDrawable(R.drawable.ic_album, null)
    errorDrawable.setTint(context.getColorCompat(R.color.gray))
    Glide.with(this)
            .load(url)
            .error(errorDrawable)
            .into(this)
}


