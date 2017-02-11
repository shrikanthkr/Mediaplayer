package com.mediaplayer.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;

import net.majorkernelpanic.streaming.Session;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shrikanth on 10/2/15.
 */
public class RadioFragment2 extends BaseFragment{
    private static final String TAG = "RADIO FRAGMENT";
    private Session mSession;
    private Button mButton1, mButton2, mButton3;

    private FFmpeg ffmpeg;
    SongInfo info;
    String path;
    List<String> commands = new ArrayList<>();
    String[] commadsArray;
    LibVLC mLibVLC = null;
    MediaPlayer mMediaPlayer = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.radio_fragment,container,false);
        mButton1 = (Button)v.findViewById(R.id.send);
        mButton2 = (Button)v.findViewById(R.id.receive);
        mButton3 = (Button)v.findViewById(R.id.stop);
        info = SongInfoDatabase.getInstance().getSongs("0").get(0);
        path = info.getData();
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receive();
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mLibVLC = null;
                mMediaPlayer = null;
            }
        });
        return v;

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
    private void send(){
        commands.add("-re");
        commands.add("-i");
        commands.add(path);
        commands.add("-f");
        commands.add("flv");
        commands.add("rtmp://ec2-35-154-98-231.ap-south-1.compute.amazonaws.com/");
        executeFFMPeg();
    }

    private void receive(){

        /*commands.add("-i");
        commands.add("rtmp://192.168.56.101/myapp/mystream");
        commands.add("-codec:a");
        commands.add("libmp3lame");
        commands.add("-qscale:a");
        commands.add("2");
        commands.add("-f");
        commands.add("mp3");
        commands.add(Environment.getExternalStorageDirectory()+"/"+"audio.mp3");
        //commands.add("-");

        executeFFMPeg();*/
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
        Uri uri = Uri.parse("rtmp://ec2-35-154-98-231.ap-south-1.compute.amazonaws.com/myapp/mystream");
        Media m = new Media(mLibVLC, uri);
        // Tell the media player to play the new Media.

        mMediaPlayer.setMedia(m);

        // Finally, play it!
        mMediaPlayer.setVideoTrackEnabled(false);

        mMediaPlayer.play();
        mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
            @Override
            public void onEvent(MediaPlayer.Event event) {
                Log.d("VLC", event.getTimeChanged() +"");
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
