package com.mediaplayer.manager;

import android.os.Environment;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.mediaplayer.application.MyApplication;
import com.mediaplayer.com.SongInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrikanth on 3/19/17.
 */

public class StreamManager {
    private final String PLAYLIST = "playlist.txt";
    private FFmpeg ffmpeg;
    static StreamManager INSTANCE;
    private List<String> commands;
    private String remotePath = "";

    private StreamManager(){
        ArrayList<String> options = new ArrayList<String>();
        options.add("--aout=opensles");
        ffmpeg = FFmpeg.getInstance(MyApplication.getContext());
        commands = new ArrayList<>();
        loadFFMpeg();
    }

    public static synchronized StreamManager getInstance(){
        if(INSTANCE == null) INSTANCE = new StreamManager();
        return INSTANCE;
    }

    public void startStreaming(List<SongInfo> songs){
        String playlistPath = createPlaylistFile(songs);
        stream("mystream", playlistPath);
        ModeManager.getInstance().setCurrentMode(ModeManager.Mode.STREAMING);
    }

    public void startRemotePlay(String streamPath){
        remotePath = streamPath;
        ModeManager.getInstance().setCurrentMode(ModeManager.Mode.REMOTE_PLAY);
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
    private void stream(String streamUrl, String playlistPath){
        commands.clear();
        commands.add("-f");
        commands.add("concat");
        commands.add("-safe");
        commands.add("0");
        commands.add("-re");
        commands.add("-i");
        commands.add(playlistPath);
        commands.add("-f");
        commands.add("flv");
        commands.add("rtmp://ec2-35-154-51-58.ap-south-1.compute.amazonaws.com/myapp/"+streamUrl);
        executeFFMPeg();
    }

    private String createPlaylistFile(List<SongInfo> songs){
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, PLAYLIST);
        String absPath = file.getAbsolutePath();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<songs.size();i++){
            SongInfo info = songs.get(i);
            builder.append("file").append(" ").append("'"+info.getData()+"'").append("\n");
        }
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(absPath);
            outputStream.write(builder.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return absPath;
    }

    private void executeFFMPeg(){
        try {
            killStream();
            // to execute "ffmpeg -version" commands you just need to pass "-version"
            String[] commadsArray = new String[commands.size()];
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
                    killStream();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            Log.e("FFMEPG","error", e);
        }

    }

    public void killStream(){
        if(ffmpeg.isFFmpegCommandRunning()) {
            ffmpeg.killRunningProcesses();
        }
    }

    public String getRemotePath() {
        return remotePath;
    }

}
