package com.em.app.behaviors

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AppbarOffsetChangeListener(private val pageIndicator: FloatingActionButton) : AppBarLayout.OnOffsetChangedListener {
    init {
        pageIndicator.hide()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        if (appBarLayout.height + verticalOffset == 0) {
            pageIndicator.show()
        } else {
            pageIndicator.hide()
        }
    }
}