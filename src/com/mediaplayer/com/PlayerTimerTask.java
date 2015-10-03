package com.mediaplayer.com;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by shrikanth on 10/3/15.
 */
public class PlayerTimerTask  extends AsyncTask<Void, Integer, Void>{

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
    @Override
    protected Void doInBackground(Void... voids) {
        while(isPlaying){
            SystemClock.sleep(250);
            publishProgress(SongsManager.getInstance().getSongCurrentPosition());
            Log.d("TimerTask",SongsManager.getInstance().getSongCurrentPosition()+"" );
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if(seekBar!=null){
            int currentPostion = values[0]/1000;
            seekBar.callfromTimerTask(currentPostion,duration);
        }
        super.onProgressUpdate(values);
    }
}
