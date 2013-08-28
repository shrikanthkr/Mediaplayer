package com.mediaplayer.db;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.mediaplayer.adapter.ReadLisstAdapter;
import com.mediaplayer.com.R;

public class ImageDownloader implements ComponentCallbacks{

	ReadLisstAdapter mAdapter = null;
	LruCache<String, Bitmap> Cache;
	// private HashMap<Integer, SoftReference<Bitmap> >Cache = null;
	private ExecutorService _exec = null;
	Context context;
	Bitmap bitmap;
	final Uri albumArtUri = Uri
			.parse("content://media/external/audio/albumart");

	public ImageDownloader(ReadLisstAdapter arg, Context context) {
		mAdapter = arg;
		// Cache=new HashMap<Integer, SoftReference<Bitmap>>();
		Cache = new LruCache<String, Bitmap>(4 * 1024 * 1024);
		this.context = context;
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
	}

	private class WorkerThread extends AsyncTask<Void, Void, Void> implements
	Runnable {

		Uri uri;

		public WorkerThread(String url, int pos) {
			uri = ContentUris.withAppendedId(albumArtUri, Long.parseLong(url));
		}

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			mAdapter.notifyDataSetChanged();
			super.onProgressUpdate(values);
		}

		@Override
		public void run() {
			try {
				Cache.put(uri.toString(),
						readDrawableFromNetwork(uri.toString()));
				publishProgress();
			} catch (NullPointerException e) {
			}

		}
	}

	public Bitmap getDrawble(String key) {
		/*try {
			return ;
		} catch (Exception e) {
			e.printStackTrace();
			return bitmap;
		}*/
		if(Cache.get(key)!=null)
			return Cache.get(key);
		else
			return bitmap;
	}

	/* method to get Image if already or to start new task to download */
	public void loadImage(int First, int Last) {
		Log.i("Downloader", "FIRST:"+First+":LAST:"+Last);
		try {
			if (_exec != null) {
				_exec.shutdownNow();
				_exec = null;
			}
			_exec = Executors.newFixedThreadPool(5);
			for (int pos = First; pos <= Last; pos++) {
				if (Cache.get(mAdapter.getUrlList().get(pos).getAlbum_id()) == null)
					_exec.execute(new WorkerThread(mAdapter.getUrlList()
							.get(pos).getAlbum_id(), pos));
			}
		} catch (Exception e) {

		}

	}// end of method

	private Bitmap readDrawableFromNetwork(String url) {
		try {

			Log.i("Image path", url);
			InputStream input = context.getContentResolver().openInputStream(
					Uri.parse(url));
			// Here you can make logic for decode bitmap for ignore oom error.
			return BitmapFactory.decodeStream(input);
		} catch (Exception e) {
			return bitmap;
		}
	}// end of method

	@Override
	public void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		Cache = null;
		bitmap=null;
		_exec=null;
		mAdapter=null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		Cache.evictAll();
	}

	
	
}