package com.mediaplayer.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.devsmart.android.ui.HorizontalListView;
import com.mediaplayer.adapter.NowPlayingHorizontalAdapter;
import com.mediaplayer.com.Music;
import com.mediaplayer.com.PlayerTimerTask;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SeekBar;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.listener.SeekbarTouchHandler;
import com.mediaplayer.listener.SlideHandler;
import com.mediaplayer.manager.BroadcastManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class NowPlayingFragment extends Fragment implements SongsManager.SongsListeners, SeekbarTouchHandler.SeekBarListeners{

    DisplayMetrics dm;
    SlideHandler slideHandler;
    float totalTranslation = 0f, maxBottom;
    ImageView playbutton_imageview, pausebutton_imageview, measure_view;
    ImageButton nextButton, prevButton;
    HorizontalListView nowplaying_horizontal;
    SeekBar seekbar;
    SeekbarTouchHandler seekbarTouochHandler;
    LinearLayout seekbar_layout, mainLayout, seekbar_layout_grey_bg;
    ViewTreeObserver vto;
    PlayerTimerTask playerTimer;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm =getResources().getDisplayMetrics();
        BroadcastManager.registerForEvent(BroadcastManager.PLAYSONG, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.APPEND_LIST, receiver);
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
        SongsManager.getInstance().setListener(this);
        return v;
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            SongInfo songInfo = (SongInfo)b.getSerializable(BroadcastManager.SONG_KEY);
            switch(action){
                case BroadcastManager.PLAYSONG:
                    break;
                case BroadcastManager.APPEND_LIST:
                    LinkedList<SongInfo> songList =(LinkedList<SongInfo>)b.getSerializable(BroadcastManager.LIST_KEY);
                    SongsManager.getInstance().appendSongs(songList);
                    break;
            }
            SongsManager.getInstance().playSelectedSong(songInfo);
            playSong();
        }
    };


    public void setViewIds(View view) {
        playbutton_imageview = (ImageView)view.findViewById(R.id.playbutton_imageView);
        pausebutton_imageview =  (ImageView)view.findViewById(R.id.pausebutton_imageView);
        nextButton = (ImageButton)view.findViewById(R.id.nextbutton);
        prevButton = (ImageButton)view.findViewById(R.id.previous_button);
        nowplaying_horizontal = (HorizontalListView) view.findViewById(R.id.nowplaying_horizontal);
        seekbar_layout = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        seekbar_layout_grey_bg = (LinearLayout) view.findViewById(R.id.seekbar_layout_grey_bg);
        measure_view = (ImageView) view.findViewById(R.id.seek_measure_imageView);
        mainLayout = (LinearLayout) view.findViewById(R.id.nowplaying_id);

        playbutton_imageview.setOnClickListener(buttonListener);
        pausebutton_imageview.setOnClickListener(buttonListener);
        nextButton.setOnClickListener(buttonListener);
        prevButton.setOnClickListener(buttonListener);
        updateNowPlayingListUI();
        setupSeekbar();
    }

    private void setupSeekbar() {
        seekbar = new SeekBar(getActivity());
        vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
        seekbarTouochHandler = new SeekbarTouchHandler(seekbar);
        seekbar_layout.setOnTouchListener(seekbarTouochHandler);
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

    ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Window window = getActivity().getWindow();
            window.setFormat(PixelFormat.RGBA_8888);
            int pos_x = seekbar_layout.getWidth() / 2;
            pos_x += 1.5;
            int pos_y = seekbar_layout.getHeight() / 2;
            seekbar.setMeasuredHeigthWidth(measure_view.getWidth(),
                    measure_view.getHeight());
            seekbar.setCenter_x(pos_x);
            seekbar.setCenter_y(pos_y);
            seekbar.setRadius((float) ((Math.min(
                    seekbar_layout_grey_bg.getWidth(),
                    seekbar_layout_grey_bg.getHeight()) / 2) + .5));
            seekbar.setXY(pos_x, pos_y);
            seekbar_layout.addView(seekbar);
            //Log.i("Radius", seekbar.radius + "");
           /* artist_header.setText(songInfo.getArtist());
            song_header.setText(songInfo.getTitle());
            duration_header.setText(0 + "");*/

            if (vto.isAlive()) {
                vto.removeGlobalOnLayoutListener(this);
                // //Log.i("Layout listener", "Removed");
                //callTimerTask();
            } else {
                vto = mainLayout.getViewTreeObserver();
                vto.removeGlobalOnLayoutListener(this);
            }
        }
    };



    @Override
    public void onSongStarted(SongInfo songInfo) {
        String durationS = SongsManager.getInstance().getCurrentSongInfo().getDuration();
        int duration =  (int)Math.ceil(Double.parseDouble(durationS) / 1000);
        playerTimer = new PlayerTimerTask(seekbar,duration);
        playerTimer.setIsPlaying(true);
        playerTimer.execute();
        seekbarTouochHandler.setDuration(duration);
        seekbarTouochHandler.removeOnSeekListener();
        seekbarTouochHandler.setOnSeekListener(this);
        playSong();
    }

    @Override
    public void onSongCompleted() {
        playNextSong();
    }

    @Override
    public void onSongChanged(SongInfo info) {
        updateNowPlayingListUI();
    }

    private void playSong(){
        playbutton_imageview.setVisibility(View.INVISIBLE);
        pausebutton_imageview.setVisibility(View.VISIBLE);
        SongsManager.getInstance().resume();
    }

    private void pauseSong(){
        playbutton_imageview.setVisibility(View.VISIBLE);
        pausebutton_imageview.setVisibility(View.GONE);
        SongsManager.getInstance().pause();
        playerTimer.setIsPlaying(false);
    }

    private void playNextSong(){
        SongsManager.getInstance().playNextSong();
    }

    private void playPreviousSong(){
        SongsManager.getInstance().playPreviousSong();
    }
    private void updateNowPlayingListUI() {
        ArrayList<SongInfo> horizontal_songInfo_array = null;
        NowPlayingHorizontalAdapter horizontal_adapter;
        horizontal_songInfo_array = new ArrayList<>(SongsManager.getInstance().getSongsList());
        horizontal_adapter = new NowPlayingHorizontalAdapter(horizontal_songInfo_array, nowplaying_horizontal, getActivity());
        nowplaying_horizontal.setAdapter(horizontal_adapter);

    }

    @Override
    public void afterSeek(int seektime) {
        SongsManager.getInstance().seekPlayerTo(seektime);
    }
}
