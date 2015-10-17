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
