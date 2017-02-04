package com.mediaplayer.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;

import net.majorkernelpanic.streaming.Session;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shrikanth on 10/2/15.
 */
public class RadioFragment2 extends BaseFragment implements Session.Callback {
    private static final String TAG = "RADIO FRAGMENT";
    private Session mSession;
    private Button mButton1;
    private EditText mEditText;
    private FFmpeg ffmpeg;
    SongInfo info;
    String path;
    List<String> commands = new ArrayList<>();
    String[] commadsArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.radio_fragment,container,false);
        mButton1 = (Button)v.findViewById(R.id.button);
        mEditText = (EditText) v.findViewById(R.id.editText1);
        info = SongInfoDatabase.getInstance().getSongs("0").get(0);
        path = info.getData();
        commands.add("-re");
        commands.add("-i");
        commands.add(path);
        commands.add("-f");
        commands.add("flv");
        commands.add("rtmp://192.168.56.102/myapp/mystream");
        return v;

    }

    @Override
    public void setTitle() {
        getActivity().setTitle(getString(R.string.albums));
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ffmpeg = FFmpeg.getInstance(getContext());
        /*mSession = SessionBuilder.getInstance()
                .setCallback(this)
                .setPreviewOrientation(90)
                .setContext(getContext())
                .setAudioEncoder(SessionBuilder.AUDIO_AAC)
                .setAudioQuality(new AudioQuality(16000, 32000))
                .setVideoEncoder(SessionBuilder.VIDEO_NONE)
                .build();
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSession.setDestination(mEditText.getText().toString());
                if (!mSession.isStreaming()) {
                    mSession.configure();
                } else {
                    mSession.stop();
                }
            }
        });*/
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
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            Log.e("FFMEPG","error", e);
        }


    }

    @Override
    public void onBitrateUpdate(long bitrate) {
        Log.d(TAG,"Bitrate: "+bitrate);
    }

    @Override
    public void onSessionError(int message, int streamType, Exception e) {
        mButton1.setEnabled(true);
        if (e != null) {
            logError(e.getMessage());
        }
    }

    @Override

    public void onPreviewStarted() {
        Log.d(TAG,"Preview started.");
    }

    @Override
    public void onSessionConfigured() {
        Log.d(TAG,"Preview configured.");
        // Once the stream is configured, you can get a SDP formated session description
        // that you can send to the receiver of the stream.
        // For example, to receive the stream in VLC, store the session description in a .sdp file
        // and open it with VLC while streming.
        Log.d(TAG, mSession.getSessionDescription());
        mSession.start();
    }

    @Override
    public void onSessionStarted() {
        Log.d(TAG,"Session started.");
        mButton1.setEnabled(true);
        mButton1.setText(R.string.stop);
    }

    @Override
    public void onSessionStopped() {
        Log.d(TAG,"Session stopped.");
        mButton1.setEnabled(true);
        mButton1.setText(R.string.start);
    }

    /** Displays a popup to report the eror to the user */
    private void logError(final String msg) {
        final String error = (msg == null) ? "Error unknown" : msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(error).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
