package com.mediaplayer.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mediaplayer.com.Music;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.listener.SlideHandler;
import com.mediaplayer.manager.BroadcastManager;

/**
 * Created by shrikanth on 10/2/15.
 */
public class NowPlayingFragment extends Fragment implements Music.MusicChangeListeners{

    DisplayMetrics dm;
    SlideHandler slideHandler;
    float totalTranslation = 0f, maxBottom;
    ImageView playbutton_imageview, pausebutton_imageview;
    ImageButton nextButton, prevButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm =getResources().getDisplayMetrics();
        BroadcastManager.registerForEvent("PLAYSONG", receiver);
        SongsManager.getInstance().setContext(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.nowplaying_xml,container,false);
        maxBottom =  dm.heightPixels - 70;
        totalTranslation = dm.heightPixels - 260;
        v.setTranslationY(totalTranslation);
        slideHandler = new SlideHandler(getActivity());
        setViewIds(v);
        v.setOnTouchListener(slideHandler);
        return v;
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            SongInfo songInfo = (SongInfo)b.getSerializable("songInfo");
            SongsManager.getInstance().playSelectedSong(songInfo);
            playSong();
        }
    };

    @Override
    public void onSongStarted(SongInfo songInfo) {

    }

    @Override
    public void onSongCompleted() {

    }

    public void setViewIds(View view) {
        playbutton_imageview = (ImageView)view.findViewById(R.id.playbutton_imageView);
        pausebutton_imageview =  (ImageView)view.findViewById(R.id.pausebutton_imageView);
        nextButton = (ImageButton)view.findViewById(R.id.nextbutton);
        prevButton = (ImageButton)view.findViewById(R.id.previous_button);

        playbutton_imageview.setOnClickListener(buttonListener);
        pausebutton_imageview.setOnClickListener(buttonListener);
        nextButton.setOnClickListener(buttonListener);
        prevButton.setOnClickListener(buttonListener);
    }
    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch(id){
                case R.id.playbutton_imageView:
                    playSong();
                    break;
                case R.id.pausebutton_imageView:
                    pauseSong();
                    break;
                case R.id.nextbutton:
                    playNextSong();
                    break;
                case R.id.previous_button:
                    playPreviousSong();
                    break;

            }
        }
    };

    private void playSong(){
        playbutton_imageview.setVisibility(View.INVISIBLE);
        pausebutton_imageview.setVisibility(View.VISIBLE);
        SongsManager.getInstance().resume();
    }

    private void pauseSong(){
        playbutton_imageview.setVisibility(View.VISIBLE);
        pausebutton_imageview.setVisibility(View.GONE);
        SongsManager.getInstance().pause();
    }

    private void playNextSong(){
        SongsManager.getInstance().playNextSong();
    }

    private void playPreviousSong(){
        SongsManager.getInstance().playPreviousSong();
    }


}
