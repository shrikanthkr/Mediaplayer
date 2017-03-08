package com.mediaplayer.fragments;


import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mediaplayer.adapter.RadioRecyclerAdapter;
import com.mediaplayer.app.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.customviews.RadioRecyclerView;
import com.mediaplayer.db.SongInfoDatabase;
import com.mediaplayer.interfaces.RecyclerClickHelper;
import com.mediaplayer.models.Radio;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by shrikanth on 10/2/15.
 */
public class RadioFragment2 extends BaseFragment{
    private static final String TAG = "RADIO FRAGMENT";

    private Button mButton1, mButton2, mButton3, mPause;

    private FFmpeg ffmpeg;
    SongInfo info;
    String path;
    List<String> commands = new ArrayList<>();
    String[] commadsArray;
    LibVLC mLibVLC = null;
    MediaPlayer mMediaPlayer = null;
    private DatabaseReference mDatabase;
    List<Radio> stations;
    RadioRecyclerView radioStationsView;
    RadioRecyclerAdapter adapter;
    LinearLayoutManager layoutManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stations = new ArrayList<>();
        adapter = new RadioRecyclerAdapter(new RecyclerClickHelper() {
            @Override
            public void onItemClickListener(View view, int position) {
                final Radio r = stations.get(position);
                showAlertDialog(r.streamTitle, "Do you want Join?", "Yes", "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                receive(r.streamUrl);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dismissAlertDialog();
                                break;
                        }
                    }
                });
            }
        });
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.radio_fragment,container,false);
        mButton1 = (Button)v.findViewById(R.id.send);
        mButton2 = (Button)v.findViewById(R.id.receive);
        mButton3 = (Button)v.findViewById(R.id.stop);
        mPause = (Button)v.findViewById(R.id.pause);
        radioStationsView = (RadioRecyclerView)v.findViewById(R.id.radio_stations);
        radioStationsView.setLayoutManager(layoutManager);
        radioStationsView.setAdapter(adapter);
        info = SongInfoDatabase.getInstance().getSongs("wwe").get(0);
        path = info.getData();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("myStream");
            }
        });

        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               play();
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mLibVLC = null;
                    mMediaPlayer = null;
                    if(ffmpeg.isFFmpegCommandRunning()) {
                        ffmpeg.killRunningProcesses();
                    }
                }
            }
        });
        mDatabase.child("radio").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "dataChange:" + dataSnapshot.getKey());
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    Radio r = iterator.next().getValue(Radio.class);
                    stations.add(r);
                }
                adapter.setStations(stations);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;

    }

    private void pause() {
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }else{
            mMediaPlayer.play();
        }

    }

    private void play() {
        ArrayList<String> options = new ArrayList<String>();
        //options.add("--subsdec-encoding <encoding>");
        options.add("--aout=opensles");
        mLibVLC = new LibVLC(getContext(), options);

        mMediaPlayer = new MediaPlayer(mLibVLC);
        final Media m = new Media(mLibVLC, info.getData());
        mMediaPlayer.setMedia(m);

        // Finally, play it!
        mMediaPlayer.setVideoTrackEnabled(false);
        m.setEventListener(new Media.EventListener() {
            @Override
            public void onEvent(Media.Event event) {
                switch (event.type){
                    case Media.Event.MetaChanged:
                        m.getMeta(event.getMetaId());
                        Log.d("VLC", "Meta Changed" + m.getMeta(event.getMetaId()));
                        break;
                    case Media.Event.DurationChanged:
                        Log.d("VLC", "Duration Changed");
                        break;
                    case Media.Event.ParsedChanged:
                        Log.d("VLC", "Parsed Changed");
                        break;
                    case Media.Event.StateChanged:
                        Log.d("VLC", "State Changed" + event.getParsedStatus()) ;
                        break;
                }
            }
        });
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                Log.d("VLC", event.type+"");
                Log.d("VLC", event.getTimeChanged()+"");


            }
        });
        mMediaPlayer.play();

        mMediaPlayer.setTime(20000);

    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.albums));
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ffmpeg = FFmpeg.getInstance(getContext());
        loadFFMpeg();
    }

    private void loadFFMpeg(){
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Log.e("FFMEPG","error", e);
        }
    }
    private void send(String streamUrl){
        commands.add("-re");
        commands.add("-i");
        commands.add(path);
        commands.add("-f");
        commands.add("flv");
        commands.add("rtmp://ec2-35-154-98-231.ap-south-1.compute.amazonaws.com/myapp/"+streamUrl);
        executeFFMPeg();
    }

    private void receive(String url){
        if(mMediaPlayer == null) {
            ArrayList<String> options = new ArrayList<String>();
            //options.add("--subsdec-encoding <encoding>");
            //options.add("--aout=opensles");
            options.add("--sout=#std{access=rtmp,mux=ffmpeg{mux=flv},dst=rtmp://ec2-35-154-98-231.ap-south-1.compute.amazonaws.com/myapp/mystream}");
            options.add("--audio-time-stretch"); // time stretching
            options.add("-vvv"); // verbosity
            mLibVLC = new LibVLC(getContext(), options);
            mMediaPlayer = new MediaPlayer(mLibVLC);
        }
        Uri uri = Uri.parse("rtmp://ec2-35-154-98-231.ap-south-1.compute.amazonaws.com/myapp/"+url);
        final Media m = new Media(mLibVLC, uri);
        // Tell the media player to play the new Media.

        mMediaPlayer.setMedia(m);

        // Finally, play it!
        mMediaPlayer.setVideoTrackEnabled(false);

        m.setEventListener(new Media.EventListener() {
            @Override
            public void onEvent(Media.Event event) {
                switch (event.type){
                    case Media.Event.MetaChanged:
                        m.getMeta(event.getMetaId());
                        Log.d("VLC", "Meta Changed" + m.getMeta(event.getMetaId()));
                        break;
                    case Media.Event.DurationChanged:
                        Log.d("VLC", "Duration Changed");
                        break;
                    case Media.Event.ParsedChanged:
                        Log.d("VLC", "Parsed Changed");
                        break;
                    case Media.Event.StateChanged:
                        Log.d("VLC", "State Changed");
                        break;
                }
            }
        });

        mMediaPlayer.play();
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                Log.d("VLC", event.type+"");
                Log.d("VLC", event.getTimeChanged()+"");
                switch (event.type){
                    case MediaPlayer.Event.Playing:
                        Log.d("VLC", "Playing");
                        break;
                    case MediaPlayer.Event.Paused:
                        Log.d("VLC", "Paused");
                        break;
                    case MediaPlayer.Event.Stopped:
                        Log.d("VLC", "Stopped");
                        break;
                    case MediaPlayer.Event.PositionChanged:
                        Log.d("VLC", "Position Changed");
                        break;
                    case MediaPlayer.Event.TimeChanged:
                        Log.d("VLC", "Time Changed" );
                        break;
                }

            }
        });

    }
    private void executeFFMPeg(){
        try {
            // to execute "ffmpeg -version" commands you just need to pass "-version"
            commadsArray = new String[commands.size()];
            commands.toArray(commadsArray);
            ffmpeg.execute(commadsArray, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    Log.d("FFMEPG","start");
                }

                @Override
                public void onProgress(String message) {
                    Log.d("FFMEPG",message);
                }

                @Override
                public void onFailure(String message) {
                    Log.e("FFMEPG",message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.d("FFMEPG",message);
                }

                @Override
                public void onFinish() {
                    Log.d("FFMEPG","finish");
                    ffmpeg.killRunningProcesses();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            Log.e("FFMEPG","error", e);
        }

    }


}
