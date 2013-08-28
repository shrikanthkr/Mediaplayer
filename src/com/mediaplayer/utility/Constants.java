package com.mediaplayer.utility;

public class Constants {

	public static final String CONSUMER_KEY = "ehTRyMT0ICb24RCSfKg";
	public static final String CONSUMER_SECRET= "bsi86bcKrpvyseCyinTPQJiXgLaU0IoSC86Z9zOiI";
	
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";
	
	public static final String	OAUTH_CALLBACK_SCHEME	= "mediaplayer-oauthflow-twitter";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}

