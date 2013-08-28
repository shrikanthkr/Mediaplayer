package com.mediaplayer.com;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;


	public class AuthActivity extends Activity {

	private static final String ACCESS_TOKEN="accessToken";
	private static final String ACCESS_TOKEN_SECRET="accessTokenSecret";
	private static final String CONSUMER_KEY="ehTRyMT0ICb24RCSfKg";
	private static final String CONSUMER_SECRET="bsi86bcKrpvyseCyinTPQJiXgLaU0IoSC86Z9zOiI";
	private static final String CALLBACK_URL="com123456789:///";

	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	private SharedPreferences init_app;
	private SharedPreferences.Editor init_editor;

	private Twitter twitter;
	private RequestToken reqtoken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs=getSharedPreferences("TwitterShared",MODE_PRIVATE);
		editor=prefs.edit();

		init_app=getSharedPreferences("Twitter_App_Init",MODE_PRIVATE);
		init_editor=init_app.edit();

		twitter=new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

		if(prefs.contains(ACCESS_TOKEN))
			AlreadyRegisteredUser();
		else
			LoginNewUser();
	}

	private void AlreadyRegisteredUser() {
		String token=prefs.getString(ACCESS_TOKEN, null);
		String secret=prefs.getString(ACCESS_TOKEN_SECRET, null);
		Log.i("AUTH ACTIVITY", "Already Registerd");
		AccessToken acc_token= new AccessToken(token,secret);
		twitter.setOAuthAccessToken(acc_token);
	}

	private void LoginNewUser() {

		try {
			reqtoken=twitter.getOAuthRequestToken(CALLBACK_URL);

			WebView twitter_webview=new WebView(this);
			//twitter_webview.getSettings().setJavaScriptEnabled(true);
			twitter_webview.loadUrl(reqtoken.getAuthorizationURL());
			setContentView(twitter_webview);
			Log.i("AUTH ACTIVITY", "Content view sert");
		} catch (TwitterException e) {
				e.printStackTrace();
		}

	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		HandleTwitterResponse(intent);
		Log.i("AUTH ACTIVITY", "Twitter handler called");

	}

	private void HandleTwitterResponse(Intent intent) {
		Uri uri=intent.getData();
			if(uri!=null && uri.toString().startsWith(CALLBACK_URL))
			{
				String oauth_verify=uri.getQueryParameter("oauth_verifier");
				authoriseNewUser(oauth_verify);
			}
	}

	private void authoriseNewUser(String oauth_verify) {
		try {
			AccessToken acc_token=twitter.getOAuthAccessToken(reqtoken, oauth_verify);
			twitter.setOAuthAccessToken(acc_token);

			saveAccessTokenForFuture(acc_token);
			Log.i("AUTH ACTIVITY", "Token saved");
			} catch (TwitterException e) {
			e.printStackTrace();
		}

	}

	private void saveAccessTokenForFuture(AccessToken acc_token) {

		String token=acc_token.getToken();
		String secret=acc_token.getTokenSecret();

		editor.putString(ACCESS_TOKEN, token);
		editor.putString(ACCESS_TOKEN_SECRET, secret);
		editor.commit();

		init_editor.putBoolean("Init_app", true);
		init_editor.commit();
		Log.i("AUTH ACTIVITY", "Finish called");
		finish();

	}

	protected void onResume() {
		super.onResume();
	}

}