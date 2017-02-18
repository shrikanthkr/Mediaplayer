package com.mediaplayer.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.mediaplayer.activities.BaseActivity;
import com.mediaplayer.app.R;
import com.mediaplayer.com.SongInfo;
import com.mediaplayer.com.SongsManager;

/**
 * Created by shrikanth on 11/15/15.
 */
public class NotificationHelper {
    private Context parent;
    private NotificationManager nManager;
    private NotificationCompat.Builder nBuilder;
    private Notification notification;
    private RemoteViews remoteView;
    private SongInfo info;
    public NotificationHelper(Context parent) {
        info = SongsManager.getInstance().getCurrentSongInfo();
        this.parent = parent;
        Intent myIntent = new Intent(parent, BaseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(parent, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        nBuilder = new NotificationCompat.Builder(parent)
                .setSmallIcon(R.drawable.albums)
                .setTicker(info.getDisplayName())
                .setContentTitle(info.getDisplayName())
                .setOngoing(true)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        remoteView = new RemoteViews(parent.getPackageName(), R.layout.notificationview);

        //set the button listeners
        setupView(remoteView);
        setListeners(remoteView);
        notification = nBuilder.build();
        notification.bigContentView = remoteView;
        nManager = (NotificationManager) parent.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(1, notification);
    }


    public void setListeners(RemoteViews view){
        //listener 1
        Intent prevIntent = new Intent();
        prevIntent.setAction(BroadcastManager.NOTIFICATION_PREV);
        prevIntent.putExtra(BroadcastManager.NOTIFICATION_HANDLER, BroadcastManager.NOTIFICATION_PREV);
        PendingIntent previous = PendingIntent.getBroadcast(parent, BroadcastManager.NOTIFICATION_REQUEST_CODE, prevIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.previous_button, previous);

        //listener 2
        Intent nextIntent = new Intent();
        nextIntent.setAction(BroadcastManager.NOTIFICATION_NEXT);
        nextIntent.putExtra(BroadcastManager.NOTIFICATION_HANDLER, BroadcastManager.NOTIFICATION_NEXT);
        PendingIntent next = PendingIntent.getBroadcast(parent, BroadcastManager.NOTIFICATION_REQUEST_CODE, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.nextbutton, next);

        Intent playIntent = new Intent();
        playIntent.setAction(BroadcastManager.NOTIFICATION_RESUME);
        playIntent.putExtra(BroadcastManager.NOTIFICATION_HANDLER, BroadcastManager.NOTIFICATION_RESUME);
        PendingIntent play = PendingIntent.getBroadcast(parent, BroadcastManager.NOTIFICATION_REQUEST_CODE, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.play_button, play);

        Intent pauseIntent = new Intent();
        pauseIntent.setAction(BroadcastManager.NOTIFICATION_PAUSE);
        pauseIntent.putExtra(BroadcastManager.NOTIFICATION_HANDLER, BroadcastManager.NOTIFICATION_PAUSE);
        PendingIntent pause = PendingIntent.getBroadcast(parent, BroadcastManager.NOTIFICATION_REQUEST_CODE, pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.pause_button, pause);

       Intent closeIntent = new Intent();
        closeIntent.setAction(BroadcastManager.NOTIFICATION_CLOSE);
        closeIntent.putExtra(BroadcastManager.NOTIFICATION_HANDLER, BroadcastManager.NOTIFICATION_CLOSE);
        PendingIntent close = PendingIntent.getBroadcast(parent, BroadcastManager.NOTIFICATION_REQUEST_CODE, closeIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        view.setOnClickPendingIntent(R.id.close, close);
    }

    public void notificationCancel() {
        nManager.cancel(1);
    }

    public void setupView(RemoteViews view) {
        view.setTextViewText(R.id.title, info.getDisplayName());
        view.setTextViewText(R.id.artist, info.getArtist());
        view.setImageViewBitmap(R.id.album_art, getImageBitmap(info.getAlbum_id()));
        if(SongsManager.getInstance().isPlaying()){
            view.setViewVisibility(R.id.play_button, View.GONE);
            view.setViewVisibility(R.id.pause_button, View.VISIBLE);
        }else{
            view.setViewVisibility(R.id.pause_button, View.GONE);
            view.setViewVisibility(R.id.play_button, View.VISIBLE);
        }
    }

    private Bitmap getImageBitmap(String id) {
        Bitmap bm ;
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.parseLong(id));
        try {
            bm =  MediaStore.Images.Media.getBitmap(parent.getContentResolver(), uri);
            if(bm==null){
                bm  =BitmapFactory.decodeResource(parent.getResources(),R.drawable.albums);
            }
        } catch (Exception e) {
            Log.e("NOTIFICATION SERVIDE", "Error getting bitmap", e);
            bm  =BitmapFactory.decodeResource(parent.getResources(),R.drawable.albums);
        }
        return bm;
    }
}
