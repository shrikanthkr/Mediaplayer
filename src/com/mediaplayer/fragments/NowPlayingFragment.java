package com.mediaplayer.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;
import com.mediaplayer.adapter.NowPlayingHorizontalAdapter;
import com.mediaplayer.com.Music;
import com.mediaplayer.com.MyApplication;
import com.mediaplayer.com.PlayerTimerTask;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SeekBar;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;
import com.mediaplayer.customviews.PlayPauseView;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.listener.SeekbarTouchHandler;
import com.mediaplayer.listener.SlideHandler;
import com.mediaplayer.manager.BroadcastManager;
import com.mediaplayer.manager.EchonestApiManager;
import com.mediaplayer.utility.AnimationUtil;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by shrikanth on 10/2/15.
 */
public class NowPlayingFragment extends Fragment implements SongsManager.SongsListeners, SeekbarTouchHandler.SeekBarListeners {

    private static final String IS_REPEAT = "repeat";
    private static final String IS_SHUFFLE = "repeat";
    DisplayMetrics dm;
    SlideHandler slideHandler;
    float totalTranslation = 0f, maxBottom;
    ImageView  measure_view;
    PlayPauseView playPauseView;
    ImageButton nextButton, prevButton, identifyButton,repeat_button,shuffle_button;
    HorizontalListView nowplaying_horizontal;
    TextView  count_label, artist_header, songname_header, duration_header,tempduration_textView;
    SeekBar seekbar;
    SeekbarTouchHandler seekbarTouochHandler;
    LinearLayout seekbar_layout, mainLayout;
    ViewTreeObserver vto;
    PlayerTimerTask playerTimer;
    View playerView;
    SharedPreferences preferences;
    SharedPreferences.Editor prefsEditor;
    NowPlayingHorizontalAdapter horizontal_adapter;
    ArrayList<SongInfo> horizontal_songInfo_array = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm =getResources().getDisplayMetrics();
        BroadcastManager.registerForEvent(BroadcastManager.PLAYSONG, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.PLAY_SELECTED, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.APPEND_LIST, receiver);
        BroadcastManager.registerForEvent(BroadcastManager.HEAD_SET_STATE_UPDATE, receiver);
        SongsManager.getInstance().setContext(getActivity());
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        prefsEditor = preferences.edit();
        SongsManager.getInstance().setIsRepeat(preferences.getBoolean(IS_REPEAT, false));
        SongsManager.getInstance().setIsShuffle(preferences.getBoolean(IS_SHUFFLE, false));

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
        SongsManager.getInstance().setListener(this);
        horizontal_songInfo_array = new ArrayList<>();
        horizontal_adapter = new NowPlayingHorizontalAdapter(horizontal_songInfo_array, nowplaying_horizontal, getActivity());
        nowplaying_horizontal.setAdapter(horizontal_adapter);
        return playerView;
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
                case BroadcastManager.PLAY_SELECTED:
                    LinkedList<SongInfo> selectedSongList =(LinkedList<SongInfo>)b.getSerializable(BroadcastManager.LIST_KEY);
                    if(selectedSongList!=null && selectedSongList.size() > 0){
                        SongsManager.getInstance().addSelectedSongs(selectedSongList);
                    }
                    break;
                case BroadcastManager.HEAD_SET_STATE_UPDATE:
                    resetState();
                    break;
            }
            if(songInfo!=null){
                SongsManager.getInstance().playSelectedSong(songInfo);
            }else{
                updateNowPlayingListUI();
            }

        }
    };


    public void setViewIds(View view) {
        playPauseView = (PlayPauseView)view.findViewById(R.id.playPauseView);
        nextButton = (ImageButton)view.findViewById(R.id.nextbutton);
        prevButton = (ImageButton)view.findViewById(R.id.previous_button);
        nowplaying_horizontal = (HorizontalListView) view.findViewById(R.id.nowplaying_horizontal);
        seekbar_layout = (LinearLayout) view.findViewById(R.id.seekbar_layout);
        measure_view = (ImageView) view.findViewById(R.id.seek_measure_imageView);
        mainLayout = (LinearLayout) view.findViewById(R.id.nowplaying_id);
        count_label = (TextView)view.findViewById(R.id.count_label);
        artist_header= (TextView)view.findViewById(R.id.artist_now_playingheader);
        songname_header = (TextView)view.findViewById(R.id.song_now_playingheader);
        duration_header = (TextView)view.findViewById(R.id.duration_header);
        identifyButton = (ImageButton)view.findViewById(R.id.identify_imageButton);
        seekbar = (SeekBar)view.findViewById(R.id.seekbar);
        tempduration_textView = (TextView) view.findViewById(R.id.tempduration_textView);
        repeat_button = (ImageButton)view.findViewById(R.id.repeat_button);
        shuffle_button= (ImageButton)view.findViewById(R.id.shuffle_button);

        nextButton.setOnClickListener(buttonListener);
        prevButton.setOnClickListener(buttonListener);
        identifyButton.setOnClickListener(buttonListener);
        playPauseView.setOnClickListener(buttonListener);
        repeat_button.setOnClickListener(buttonListener);
        shuffle_button.setOnClickListener(buttonListener);
        setupSeekbar();
        setButtonState();
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
                case R.id.identify_imageButton:
                    AnimationUtil.startRotation(identifyButton,getActivity());
                    EchonestApiManager.uploadTrack(SongsManager.getInstance().getCurrentSongInfo().data, new EchonestApiManager.EchonestApiListener() {
                        @Override
                        public void onResult(Track track) {
                            try {
                                if(track.getStatus().ordinal()==2){
                                    Log.d("NOW", track.getReleaseName());
                                    SongInfo songInfo = SongsManager.getInstance().getCurrentSongInfo();
                                    songInfo.setArtist(track.getArtistName());
                                    songInfo.setDisplayName(track.getTitle());
                                    songInfo.setTitle(track.getTitle());
                                    SongInfoDatabase db = new SongInfoDatabase();
                                    db.open();
                                    db.update(songInfo);
                                    db.close();updateSongInfo();
                                    Toast.makeText(getActivity(),"Song Updated" , Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getActivity(),"Sorry song was not found" , Toast.LENGTH_LONG).show();
                                }
                            } catch (EchoNestException e) {
                                e.printStackTrace();
                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(),"Sorry song was not found" , Toast.LENGTH_LONG).show();
                            }
                            AnimationUtil.stopRotation(identifyButton);
                        }
                    });
                    break;
                case R.id.repeat_button:
                    toggleRepeat();
                    break;
                case R.id.shuffle_button:
                    toggleShuffle();
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
        prefsEditor.putBoolean(IS_REPEAT, SongsManager.getInstance().isRepeat());
        prefsEditor.putBoolean(IS_SHUFFLE, SongsManager.getInstance().isShuffle());
        prefsEditor.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetState();
    }

    private void updateUI(){
        if(!MyApplication.isActivityVisible()){
            return;
        }
        updateNowPlayingListUI();
        updateSongInfo();
    }

    private void resetState() {
        SongInfo info  = SongsManager.getInstance().getCurrentSongInfo();
        if(SongsManager.getInstance().isPlaying()){
            if(info!=null && MyApplication.isActivityVisible() ){
                onSongStarted(info);
            }
            playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PLAYING);
        }else{
            playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PAUSED);
        }
    }

    @Override
    public void onSongStarted(SongInfo songInfo) {
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
            updateUI();
            playSong();
        }

    }

    @Override
    public void onSongChanged(SongInfo info) {
        updateUI();
    }

    @Override
    public void onSongAdded(SongInfo songInfo) {
        Toast.makeText(getActivity(),songInfo.getTitle() + " Added",Toast.LENGTH_LONG).show();
        updateUI();
        playSong();
    }

    @Override
    public void onSongCompleted() {
        playNextSong();
    }

    private void pauseSong(){
        SongsManager.getInstance().pause();
        playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PAUSED);
        playerTimer.setIsPlaying(false);
    }

    private void playSong(){
        playPauseView.togglePlayPauseButton(PlayPauseView.ROTATESTATE.PLAYING);
        SongsManager.getInstance().resume();
    }

    private void playPauseSong(){
        if( SongsManager.getInstance().isPlaying()){
                pauseSong();
        }else{
                playSong();
        }
    }

    private void playNextSong(){
        SongsManager.getInstance().playNextSong();
    }

    private void playPreviousSong(){
        SongsManager.getInstance().playPreviousSong();
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
}
