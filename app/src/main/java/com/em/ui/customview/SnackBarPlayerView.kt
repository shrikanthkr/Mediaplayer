package com.em.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.em.app.R
import com.google.android.material.snackbar.ContentViewCallback

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