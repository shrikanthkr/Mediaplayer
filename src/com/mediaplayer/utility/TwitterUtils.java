package com.mediaplayer.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import com.mediaplayer.com.SongInfo;

import oauth.signpost.OAuth;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class TwitterUtils {
	public static boolean isAuthenticated(SharedPreferences prefs) {

		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		Log.i("TOKEN", token);
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);

		try {
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void sendTweet(SharedPreferences prefs,SongInfo	songInfo,Activity activity) throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();

		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		Log.i("Sending tweet", songInfo.getTitle());

		StatusUpdate update=new StatusUpdate("Hearing Now  : " + songInfo.getTitle()+" #myappname");
		twitter.updateStatus(update);
	}	

	public static String getPath(Uri uri,Activity activity) 
	{
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = 
				activity.managedQuery(uri, projection, null, null, null);


		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		String s=null;
		if( cursor != null && cursor.moveToFirst() ){
			s=cursor.getString(column_index); 
			cursor.close();
		}

		return s;
	}
}
