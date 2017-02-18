package com.mediaplayer.utility;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mediaplayer.app.R;

/**
 * Created by shrikanth on 10/4/15.
 */
public class AnimationUtil {
    public static void startRotation(View v, Context context){
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        v.startAnimation(rotation);
    }

    public static void stopRotation(View v){
        v.getAnimation().cancel();
    }
}
