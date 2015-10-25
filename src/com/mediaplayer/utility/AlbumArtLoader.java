package com.mediaplayer.utility;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.LruCache;
import android.widget.ImageView;

import com.mediaplayer.com.R;
import com.mediaplayer.db.SongInfoDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by shrikanth on 10/2/15.
 */
public class AlbumArtLoader extends AsyncTask<String, Void, Bitmap> {

    public enum Mode{
        ALBUM, ARTIST, PLAYLIST, UNKNOWN
    }
    Mode current = Mode.UNKNOWN;
    private ImageView imageView;
    private String id;
    private int width,height;
    Activity activity;
    static LruCache<String, Bitmap> cache = new LruCache<>(4  *1024);
    public AlbumArtLoader(Activity activity, String albumId, ImageView imageView, Mode mode) {
        this.id = albumId;
        this.imageView = imageView;
        this.activity = activity;
        this.current = mode;
        width = imageView.getDrawable().getIntrinsicWidth();
        height = imageView.getDrawable().getIntrinsicHeight();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        switch (current){
            case ALBUM:
                id =  id;
                break;
            case ARTIST:
                id = SongInfoDatabase.getInstance().getAlbumIdForArtist(id);
                break;
            case PLAYLIST:
                break;
            default:
                id = id;
                break;
        }
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.parseLong(id) );
        Bitmap bitmap;
        try {
            bitmap = cache.get(id);
            if(bitmap==null){
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
                bitmap = Bitmap.createScaledBitmap(bitmap, width,height, true);
                cache.put(id,bitmap);
            }

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
