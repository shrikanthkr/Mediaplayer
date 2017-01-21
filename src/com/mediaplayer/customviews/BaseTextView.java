package com.mediaplayer.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by shrikanth on 1/21/17.
 */

public class BaseTextView extends TextView {


    public BaseTextView(Context context) {
        super(context);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }



}
