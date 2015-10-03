package com.mediaplayer.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.mediaplayer.com.SeekBar;
import com.mediaplayer.fragments.NowPlayingFragment;

/**
 * Created by shrikanth on 10/3/15.
 */
public class SeekbarTouchHandler implements OnTouchListener {
    SeekBar seekbar;
    int duration;
    boolean seekablePosition = false;
    SeekBarListeners onSeekListener;

    public SeekbarTouchHandler(SeekBar seekbar) {
        this.seekbar = seekbar;
    }
    public void setDuration(int duration){
        this.duration = duration;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        float x1 = motionEvent.getX();
        float y1 = motionEvent.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                seekablePosition = positionSeekable(x1,y1);
                break;
            case MotionEvent.ACTION_MOVE:
                if(seekablePosition){
                    seekbar.calculateTempSeek(x1, y1);
                    int temp = seekbar.getSeekedTime(duration);
                }
                break;
            case MotionEvent.ACTION_UP:
                seekbar.setSeeking(false);
                seekablePosition = false;
                int temp = seekbar.getSeekedTime(duration);
                if(onSeekListener!=null) onSeekListener.afterSeek(temp);
                break;
        }
        return true;
    }

    private boolean positionSeekable(float x1, float y1) {
        // TODO Auto-generated method stub
        float allowed_position = (float) Math.sqrt(Math.pow(
                (x1 - seekbar.getCenter_x()), 2)
                + Math.pow((y1 - seekbar.getCenter_y()), 2));
        if (allowed_position < seekbar.getRadius() + 40) {
            return true;
        }
        return false;
    }

    public void setOnSeekListener(NowPlayingFragment onSeekListener) {
        this.onSeekListener = onSeekListener;
    }

    public void removeOnSeekListener() {
        onSeekListener = null;
    }

    public interface SeekBarListeners{
        void afterSeek(int seektime);
    }
}
