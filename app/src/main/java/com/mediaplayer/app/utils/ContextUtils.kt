package com.mediaplayer.app.utils

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes

fun Context.getColorCompat(@ColorRes res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res, null)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(res)
    }
}