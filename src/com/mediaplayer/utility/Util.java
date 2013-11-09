package com.mediaplayer.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.MediaStore;

import com.mediaplayer.com.SongInfo;
import com.mediaplayer.db.SongInfoDatabase;

public class Util {
	public static String TO_UPLOAD = "song";
	String fileName;
	public static HashMap<String, Bitmap> art_work;

	public void saveToExternalStoragePrivateFile(Context context,
			Object fullsongInfo, String type) {
		// Create a path where we will place our private file on external
		// storage.
		fileName = type;
		File file = new File(context.getExternalFilesDir(null), fileName);
		try {
			FileOutputStream os = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(os); // Select where
			// you wish
			// to save
			// the
			// file...
			oos.writeObject(fullsongInfo); // write the class as an 'object'
			oos.flush(); // flush the stream to insure all of the information
			// was written to 'save.bin'
			oos.close();
			// //Log.i("SD CARD WRITTEN", file.getAbsolutePath() + "");
		} catch (IOException e) {
			// Unable to create file, likely because external storage is
			// not currently mounted.
			// //Log.w("ExternalStorage", "Error writing " + file, e);
		}
	}

	public Object loadFromExternalStorage(Context context, String type) {
		fileName = type;
		// //Log.i("READ PATH", fileName);
		final File file = new File(context.getExternalFilesDir(null), fileName);
		FileInputStream fis;
		ObjectInputStream ois;
		Object object = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			ois.close();
			// //Log.i("UTIL", "SD CARD READ");
			return object;
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return object;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return object;
		}
	}

	public static boolean isForeground(String myPackage, Activity activity) {
		ActivityManager am = (ActivityManager) activity
				.getSystemService(Activity.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTaskInfo = am
				.getRunningTasks(1);
		ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		if (componentInfo.getPackageName().equals(myPackage))
			return true;
		return false;
	}

	public ArrayList<SongInfo> getAllmusic(Context context,
			ContentResolver contentResolver) {
		ArrayList<SongInfo> sgList = new ArrayList<SongInfo>();
		String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 AND LOWER("
				+ MediaStore.Audio.Media.DISPLAY_NAME
				+ ") NOT LIKE  LOWER('%.wma') ";
		String sortOrder = "UPPER(" + MediaStore.Audio.Media.TITLE + ")";
		SongInfo temp;
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
				MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ALBUM_ID, };
		Cursor cursor = contentResolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
				selection, null, sortOrder);
		while (cursor.moveToNext()) {
			// //Log.i("QUERY", cursor.toString());
			temp = new SongInfo();
			try {
				// //Log.i("Disp name",
				// cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
				temp.setDisplayName(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
			} catch (Exception e) {
				temp.setDisplayName("No display name");
			}
			try {
				// //Log.i("Title", cursor.getString(cursor
				// .getColumnIndex(MediaStore.Audio.Media.TITLE)));
				temp.setTitle(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			} catch (Exception e) {
				temp.setTitle("no Title");
			}
			try {
				// ////Log.i("ID",
				// cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
				temp.setId(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID)));
			} catch (Exception e) {
				temp.setId("No ID");
			}
			try {
				// //Log.i("Album",
				// cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
				temp.setAlbum(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
			} catch (Exception e) {
				temp.setAlbum("No Album");
			}
			try {
				// //Log.i("Artist",
				// cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
				temp.setArtist(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
			} catch (Exception e) {
				temp.setArtist("No Artist");
			}
			try {
				// //Log.i("Data", cursor.getString(cursor
				// .getColumnIndex(MediaStore.Audio.Media.DATA)));
				temp.setData(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA)));
			} catch (Exception e) {
				temp.setData("No Data");
			}
			try {
				// //Log.i("Duration", cursor.getString(cursor
				// .getColumnIndex(MediaStore.Audio.Media.DURATION)));
				temp.setDuration(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DURATION)));
			} catch (Exception e) {
				temp.setDuration("NIL");
			}
			try {
				// //Log.i("Album id", cursor.getString(cursor
				// .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
				temp.setAlbum_id(cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
			} catch (Exception e) {
				temp.setId("NIL");
			}
			temp.setAlbum_art("NIL");
			sgList.add(temp);
		}
		return sgList;
	}

	public static void deleteTrack(Context context, String localPath) {
		context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.DATA +"=\""+localPath+"\"", null);
		
	}
	
	public  static void updateTrack(Context context, SongInfo songInfo) {
		ContentValues cv = new ContentValues();
		cv.put(MediaStore.Audio.Media.ALBUM, songInfo.getAlbum());
		cv.put(MediaStore.Audio.Media.ALBUM_ID, songInfo.getAlbum_id());
		cv.put(MediaStore.Audio.Media.ARTIST, songInfo.getArtist());
		cv.put(MediaStore.Audio.Media.DATA, songInfo.getData());
		cv.put(MediaStore.Audio.Media.DISPLAY_NAME, songInfo.getDisplayName());
		cv.put(MediaStore.Audio.Media.DURATION,songInfo.getDuration());
		cv.put(MediaStore.Audio.Media._ID, songInfo.getId());
		cv.put(MediaStore.Audio.Media.TITLE, songInfo.getTitle());
		context.getContentResolver().update(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cv, MediaStore.Audio.Media.DATA +"='"+songInfo.getData()+"'", null);
		SongInfoDatabase database=new SongInfoDatabase(context);
		database.open();
		database.update(songInfo);
		database.close();
		
	}

	public static String toSqlArray(ArrayList<String> strings) {
		StringBuilder sb = new StringBuilder();
		boolean doneOne = false;
		for (String str : strings) {
			if (doneOne) {
				sb.append(", ");
			}
			sb.append("'").append(str).append("'");
			doneOne = true;
		}
		return sb.toString();
	}

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}

	public String getExtensionFromFilename(String filename) {
		return filename.substring(filename.lastIndexOf('.'), filename.length());
	}

	public String makeRingtoneFilename(CharSequence title, String extension) {
		String parentdir;
		parentdir = Environment.getExternalStorageDirectory().getPath()
				+ "/media/audio/music";
		// Create the parent directory
		File parentDirFile = new File(parentdir);
		parentDirFile.mkdirs();
		// If we can't write to that special path, try just writing
		// directly to the sdcard
		if (!parentDirFile.isDirectory()) {
			parentdir = Environment.getExternalStorageDirectory().getPath();
		}
		// Turn the title into a filename
		String filename = "";
		for (int i = 0; i < title.length(); i++) {
			if (Character.isLetterOrDigit(title.charAt(i))) {
				filename += title.charAt(i);
			}
		}
		// Try to make the filename unique
		String path = null;
		for (int i = 0; i < 100; i++) {
			String testPath;
			if (i > 0)
				testPath = parentdir + "/" + filename + i + extension;
			else
				testPath = parentdir + "/" + filename + extension;
			try {
				RandomAccessFile f = new RandomAccessFile(new File(testPath),
						"r");
			} catch (Exception e) {
				// Good, the file didn't exist
				path = testPath;
				break;
			}
		}
		return path;
	}
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isAvailable() && netInfo.isConnected();
	}
}
