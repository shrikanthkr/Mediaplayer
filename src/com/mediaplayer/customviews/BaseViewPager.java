package com.mediaplayer.customviews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by shrikanth on 1/21/17.
 */

public class BaseViewPager extends ViewPager {
    public BaseViewPager(Context context) {
        super(context);
    }

    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
