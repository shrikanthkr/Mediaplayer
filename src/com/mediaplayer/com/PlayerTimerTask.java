package com.mediaplayer.com;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shrikanth on 10/3/15.
 */
public class PlayerTimerTask  extends Timer {

    SeekBar seekBar;
    boolean isPlaying = false;
    int duration;
    MyTask myTask;
    TimerListener listener;


    public PlayerTimerTask(SeekBar seekBar,int duration,TimerListener listener) {
        this.seekBar = seekBar;
        this.duration  = duration ;
        this.listener = listener;
    }

    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public void execute(){
        myTask = new MyTask();
        this.scheduleAtFixedRate(myTask, 0, 200);
    }

    public void cancel(){
        myTask.cancel();
    }

    public class MyTask extends TimerTask{

        @Override
        public void run() {
            int currentPostion =  SongsManager.getInstance().getSongCurrentPosition()/1000;
            seekBar.callfromTimerTask(currentPostion, duration);
            if(listener!=null) {
                String duration = currentPostion/60 + ":" + currentPostion%60;
                listener.onTimerUpdate(duration);
            }
        }
    }

    public interface TimerListener {
        void onTimerUpdate(String duration);
    }

}
