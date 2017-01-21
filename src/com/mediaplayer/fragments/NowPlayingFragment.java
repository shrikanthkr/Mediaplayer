package com.mediaplayer.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaplayer.adapter.NowPlayingHorizontalAdapter;
import com.mediaplayer.com.MyApplication;
import com.mediaplayer.com.PlayerTimerTask;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SeekBar;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.com.SongsShowActivity;
import com.mediaplayer.customviews.PlayPauseView;
import com.mediaplayer.listener.SeekbarTouchHandler;
import com.mediaplayer.listener.SlideHandler;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.manager.PreferenceManager;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class NowPlayingFragment extends Fragment implements  SeekbarTouchHandler.SeekBarListeners {

    private static final String IS_REPEAT = "repeat";
    DisplayMetrics dm;
    SlideHandler slideHandler;
    float totalTranslation = 0f, maxBottom;
    ImageView  measure_view;
    PlayPauseView playPauseView;
    ImageButton nextButton, prevButton, identifyButton,repeat_button,shuffle_button, playlistCreateButton;
    RecyclerView nowplayingHorizontal;
    TextView  count_label, artist_header, songname_header, duration_header,tempduration_textView;
    SeekBar seekbar;
    SeekbarTouchHandler seekbarTouochHandler;
    LinearLayout seekbar_layout, mainLayout;
    ViewTreeObserver vto;
    PlayerTimerTask playerTimer;
    View playerView;
    NowPlayingHorizontalAdapter horizontal_adapter;
    ArrayList<SongInfo> horizontal_songInfo_array = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm =getResources().getDisplayMetrics();
        SongsManager.getInstance().setIsRepeat(PreferenceManager.INSTANCE.getIsRepeat());
        SongsManager.getInstance().setIsShuffle(PreferenceManager.INSTANCE.getIsShuffle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        playerView = inflater.inflate(R.layout.nowplaying_xml,container,false);
        maxBottom =  dm.heightPixels - 80 * dm.density;
        totalTranslation =maxBottom;
        playerView.setTranslationY(totalTranslation);
        slideHandler = new SlideHandler(getActivity());
        slideHandler.setParent(this);
        setViewIds(playerView);
        playerView.setOnTouchListener(slideHandler);
        horizontal_songInfo_array = new ArrayList<>();
        horizontal_adapter = new NowPlayingHorizontalAdapter(horizontal_songInfo_array, nowplayingHorizontal, getActivity());
        nowplayingHorizontal.setAdapter(horizontal_adapter);
        return playerView;
    }

    private void registerListeners(){
        BroadcastManager.registerForEvent(BroadcastManager.PLAYSONG, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.PLAY_SELECTED, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.APPEND_LIST, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.HEAD_SET_STATE_UPDATE, receiver);
        getActivity().registerReceiver(notificationReceiver, new IntentFilter(BroadcastManager.NOTIFICATION_HANDLER));
        getActivity().registerReceiver(notificationReceiver, new IntentFilter(BroadcastManager.NOTIFICATION_UPDATE_SONGINFO));
        getActivity().registerReceiver(notificationReceiver, new IntentFilter(BroadcastManager.NOTIFICATION_UPDATE_LIST));
        getActivity().registerReceiver(notificationReceiver, new IntentFilter(BroadcastManager.NOTIFICATION_UPDATE_PLAYPAUSE));
    }
    private void deRegisterListerners(){
        BroadcastManager.unRegisters(receiver);
        getActivity().unregisterReceiver(notificationReceiver);
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
                    if(songList!=null && songList.size() > 0)
                        SongsManager.getInstance().appendSongs(songList);
                    break;

                case BroadcastManager.HEAD_SET_STATE_UPDATE:
                    resetState();
                    break;
            }
            if(songInfo!=null){
                playSong(songInfo);
            }else{
                updateNowPlayingListUI();
            }

        }
    };


    public void setViewIds(View view) {
        playPauseView = (PlayPauseView)view.findViewById(R.id.playPauseView);
        nextButton = (ImageButton)view.findViewById(R.id.nextbutton);
        prevButton = (ImageButton)view.findViewById(R.id.previous_button);
        nowplayingHorizontal = (RecyclerView) view.findViewById(R.id.nowplaying_horizontal);
        seekbar_layout = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        measure_view = (ImageView) view.findViewById(R.id.seek_measure_imageView);
        mainLayout = (LinearLayout) view.findViewById(R.id.nowplaying_id);
        count_label = (TextView)view.findViewById(R.id.count_label);
        artist_header= (TextView)view.findViewById(R.id.artist_now_playingheader);
        songname_header = (TextView)view.findViewById(R.id.song_now_playingheader);
        duration_header = (TextView)view.findViewById(R.id.duration_header);
        //identifyButton = (ImageButton)view.findViewById(R.id.identify_imageButton);
        seekbar = (SeekBar)view.findViewById(R.id.seekbar);
        tempduration_textView = (TextView) view.findViewById(R.id.tempduration_textView);
        repeat_button = (ImageButton)view.findViewById(R.id.repeat_button);
        shuffle_button = (ImageButton)view.findViewById(R.id.shuffle_button);
        playlistCreateButton = (ImageButton)view.findViewById(R.id.playlist_create);

        nextButton.setOnClickListener(buttonListener);
        prevButton.setOnClickListener(buttonListener);
       // identifyButton.setOnClickListener(buttonListener);
        playPauseView.setOnClickListener(buttonListener);
        repeat_button.setOnClickListener(buttonListener);
        shuffle_button.setOnClickListener(buttonListener);
        playlistCreateButton.setOnClickListener(buttonListener);
        setupSeekbar();
        setButtonState();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        nowplayingHorizontal.setLayoutManager(layoutManager);

    }

    private void setButtonState() {
        if(SongsManager.getInstance().isRepeat()){
            repeat_button.setBackground(getResources().getDrawable(R.drawable.repeat));
        }else{
            repeat_button.setBackground(getResources().getDrawable(R.drawable.repeat_off));
        }

        if(SongsManager.getInstance().isShuffle()){
            shuffle_button.setBackground(getResources().getDrawable(R.drawable.shuffle));
        }else{
            shuffle_button.setBackground(getResources().getDrawable(R.drawable.shuffle_off));
        }
    }

    private void setupSeekbar() {
        vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
        seekbarTouochHandler = new SeekbarTouchHandler(seekbar);
        seekbar_layout.setOnTouchListener(seekbarTouochHandler);
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if(SongsManager.getInstance().getSongsList().size() <=0){
                return ;
            }
            switch(id){
                case R.id.playPauseView:
                    playPauseSong();
                    break;
                case R.id.nextbutton:
                    playNextSong();
                    break;
                case R.id.previous_button:
                    playPreviousSong();
                    break;
                case R.id.repeat_button:
                    toggleRepeat();
                    break;
                case R.id.shuffle_button:
                    toggleShuffle();
                    break;
                case R.id.playlist_create:
                    Intent i = new Intent(getActivity(),SongsShowActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable(SongsShowActivity.MODE_KEY, SongsShowActivity.SHOW_MODE.NOW_PLAYING);
                    i.putExtras(b);
                    startActivity(i);
                    break;

            }
        }
    };

    private void toggleRepeat() {
        boolean isRepeat = SongsManager.getInstance().isRepeat();
        isRepeat = isRepeat ? false: true;
        SongsManager.getInstance().setIsRepeat(isRepeat);
        setButtonState();
    }

    private void toggleShuffle(){
        boolean isShuffle = SongsManager.getInstance().isShuffle();
        if(!isShuffle){
            SongsManager.getInstance().shuffleSongs();
            updateUI();
        }
        isShuffle = isShuffle ? false: true;
        SongsManager.getInstance().setIsShuffle(isShuffle);
        setButtonState();

    }

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
                    seekbar_layout.getWidth() - 15,
                    seekbar_layout.getHeight()) / 2) + .5) - 15);
            seekbar.setXY(pos_x, pos_y);

            if (vto.isAlive()) {
                vto.removeGlobalOnLayoutListener(this);
            } else {
                vto = mainLayout.getViewTreeObserver();
                vto.removeGlobalOnLayoutListener(this);
            }
            seekbar.setVisibility(View.INVISIBLE);
            resetState();


        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.INSTANCE.setIsRepeat(SongsManager.getInstance().isRepeat());
        PreferenceManager.INSTANCE.setIsShuffle( SongsManager.getInstance().isShuffle());
        deRegisterListerners();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerListeners();
        resetState();
    }

    private void updateUI(){
        if(!MyApplication.isActivityVisible()){
            return;
        }
        updateNowPlayingListUI();
        updateSongInfo();
        updateSeekbar();
    }


    private void resetState() {
        SongInfo info  = SongsManager.getInstance().getCurrentSongInfo();
        if(SongsManager.getInstance().isPlaying()){
            if(info!=null && MyApplication.isActivityVisible() ){
                updateUI();
            }
            playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PLAYING);
        }else{
            playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PAUSED);
        }
    }

    public void updateSeekbar() {
        SongInfo songInfo = SongsManager.getInstance().getCurrentSongInfo();
        if(songInfo==null){
            return;
        }
        String durationS = songInfo.getDuration();
        int duration =  (int)Math.ceil(Double.parseDouble(durationS) / 1000);
        if(playerTimer!=null) {
            playerTimer.cancel();
            playerTimer.purge();
        }
        if(MyApplication.isActivityVisible()){
            seekbar.setVisibility(View.VISIBLE);
            playerTimer = new PlayerTimerTask(seekbar,duration,timerListener);
            playerTimer.setIsPlaying(true);
            playerTimer.execute();
            seekbarTouochHandler.setDuration(duration);
            seekbarTouochHandler.removeOnSeekListener();
            seekbarTouochHandler.setOnSeekListener(this);
            if(SongsManager.getInstance().isPlaying()){
                playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PLAYING);
            }else{
                playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PAUSED);
            }
        }

    }


    private void pauseSong(){
        getActivity().sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_PAUSE));
        playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PAUSED);
        playerTimer.setIsPlaying(false);
    }
    private void resumeSong(){
        playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PLAYING);
        getActivity().sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_RESUME));
    }

    private void playSong(SongInfo songInfo){
        playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PLAYING);
        SongsManager.getInstance().playSelectedSong(songInfo);
        getActivity().sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_PLAY));
    }

    private void playPauseSong(){
        if( SongsManager.getInstance().isPlaying()){
                pauseSong();
        }else{
            resumeSong();
        }
    }

    private void playNextSong(){
        getActivity().sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_NEXT));
    }

    private void playPreviousSong(){
        getActivity().sendBroadcast(new Intent(BroadcastManager.NOTIFICATION_PREV));
    }
    private void updateSongInfo(){
        SongInfo currentSong = SongsManager.getInstance().getCurrentSongInfo();
        artist_header.setText(currentSong.getArtist());
        songname_header.setText(currentSong.getDisplayName());

    }
    private void updateNowPlayingListUI() {
        horizontal_songInfo_array = new ArrayList<>(SongsManager.getInstance().getSongsList());
        horizontal_adapter.addAll(horizontal_songInfo_array);
        //nowplaying_horizontal.setAdapter(horizontal_adapter);
        count_label.setText("Queue (" + horizontal_songInfo_array.size() +")");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(playerTimer!=null) playerTimer.cancel();
    }

    public void slideDownPlayer(){
        slideHandler.slideDown(playerView);
    }

    public void slideUpPlayer(){
        slideHandler.slideUp(playerView);
    }

    @Override
    public void onSeek(int duration) {
        tempduration_textView.setVisibility(View.VISIBLE);
        tempduration_textView.setText(duration/60 + ":" + duration%60);
    }

    @Override
    public void afterSeek(int seektime) {
        SongsManager.getInstance().seekPlayerTo(seektime);
        tempduration_textView.setVisibility(View.GONE);
    }

    boolean isUp = false;
    public void setIsUp(boolean b){
        isUp = b;
    }
    public boolean getIsUp() {
        return isUp;
    }

    PlayerTimerTask.TimerListener timerListener = new PlayerTimerTask.TimerListener() {
        @Override
        public void onTimerUpdate(final String duration) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    duration_header.setText(duration);
                }
            });
        }
    };

    BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case BroadcastManager.NOTIFICATION_HANDLER:
                    updateUI();
                    break;
                case BroadcastManager.NOTIFICATION_UPDATE_LIST:
                    updateNowPlayingListUI();
                    break;
                case BroadcastManager.NOTIFICATION_UPDATE_PLAYPAUSE:
                    updateSeekbar();
                    break;
            }
        }
    };

}
