package com.mediaplayer.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import com.mediaplayer.app.R

class SnackBarPlayerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {


    init {
        View.inflate(context, R.layout.player_view, this)
    }

    override fun animateContentOut(delay: Int, duration: Int) {

    }

    override fun animateContentIn(delay: Int, duration: Int) {

    }

}