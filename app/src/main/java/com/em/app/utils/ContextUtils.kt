package com.em.app.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getColorCompat(@ColorRes res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res, null)
    } else {
        @Suppress("DEPRECATION")
        resources.getColor(res)
    }
}

fun Context.getColorInt(@AttrRes res: Int): Int {
    val typedValue = TypedValue()
    val theme: Resources.Theme = this.theme
    theme.resolveAttribute(res, typedValue, true)
    return typedValue.data
}

fun Context.getTintedDrawable(@DrawableRes res: Int, @AttrRes attrInt: Int): Drawable {
    val drawable = requireNotNull(ContextCompat.getDrawable(this, res))
    val colorInt = getColorInt(attrInt)
    val mutableDrawable = DrawableCompat.wrap(drawable).mutate()
    DrawableCompat.setTint(mutableDrawable, this.getColorInt(colorInt))
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    return mutableDrawable
}