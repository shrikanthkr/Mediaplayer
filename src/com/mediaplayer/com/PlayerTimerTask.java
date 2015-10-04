package com.mediaplayer.com;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

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


    public PlayerTimerTask(SeekBar seekBar,int duration) {
        this.seekBar = seekBar;
        this.duration  = duration ;
    }

    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public void execute(){
        myTask = new MyTask();
        this.scheduleAtFixedRate(myTask, 0, 1000);
    }

    public void cancel(){
        myTask.cancel();
    }

    public class MyTask extends TimerTask{

        @Override
        public void run() {
            int currentPostion =  SongsManager.getInstance().getSongCurrentPosition()/1000;
            seekBar.callfromTimerTask(currentPostion, duration);
        }
    }

}
