package com.mediaplayer.utility;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.mediaplayer.com.R;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by shrikanth on 10/2/15.
 */
public class ThumbnailLoader extends AsyncTask<String, Void, Bitmap> {

    private ImageView imageView;
    private String albumId;
    final static Uri albumArtUri = Uri
            .parse("content://media/external/audio/albumart");
    Activity activity;

    public ThumbnailLoader(Activity activity, String albumId, ImageView imageView) {
        this.albumId = albumId;
        this.imageView = imageView;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, Long.parseLong(albumId) );
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        } catch (Exception e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.albums);
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);
    }


}
