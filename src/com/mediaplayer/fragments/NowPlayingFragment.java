package com.mediaplayer.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mediaplayer.com.R;

/**
 * Created by shrikanth on 10/2/15.
 */
public class NowPlayingFragment extends Fragment{
    DisplayMetrics dm;
    float prevTouchY=0f;
    float currentTouchY, totalTranslation = 0f;
    enum ActionType  {UP,DOWN};
    ActionType currentAction;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         dm =getActivity().getResources().getDisplayMetrics();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.nowplaying_xml,container,false);
        totalTranslation = dm.heightPixels - 260;
        v.setTranslationY(totalTranslation);
        v.setOnTouchListener(touchListener);
        return v;
    }


    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    prevTouchY =  motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentTouchY = motionEvent.getRawY();
                    totalTranslation +=currentTouchY - prevTouchY;
                    view.setTranslationY(totalTranslation);
                    if(currentTouchY<prevTouchY){
                        currentAction = ActionType.UP;
                    }
                    if(currentTouchY>prevTouchY){
                        currentAction = ActionType.DOWN;
                    }
                    prevTouchY = currentTouchY;
                    break;
                case MotionEvent.ACTION_UP:
                    switch (currentAction){
                        case UP:
                            translateUp(view);
                            break;
                        case DOWN:
                            translateDown(view);
                            break;
                        default:
                            translateDown(view);
                            break;
                    }
                    break;
            }
            return true;
        }
    };
    private void translateUp(View v){
        totalTranslation = 0;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", totalTranslation);
        translationY.setInterpolator(new AccelerateInterpolator());

        AnimatorSet as = new AnimatorSet();
        as.playTogether(translationY);
        as.setDuration(200);
        as.start();
    }
    private void translateDown(View v){
        totalTranslation = dm.heightPixels - 260;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", totalTranslation);
        translationY.setInterpolator(new AccelerateInterpolator());

        AnimatorSet as = new AnimatorSet();
        as.playTogether(translationY);
        as.setDuration(200);
        as.start();
    }


}
