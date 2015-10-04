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


    public PlayerTimerTask(SeekBar seekBar,int duration) {
        this.seekBar = seekBar;
        this.duration  = duration ;
    }

    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
    }

    public void execute(){
        this.scheduleAtFixedRate(new MyTask(),0, 1000);
    }

    public void cancel(){

    }

    public class MyTask extends TimerTask{

        @Override
        public void run() {
            int currentPostion =  SongsManager.getInstance().getSongCurrentPosition()/1000;
            seekBar.callfromTimerTask(currentPostion, duration);
        }
    }

}
