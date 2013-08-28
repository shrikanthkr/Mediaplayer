/**
 * Copyright 2010-present Facebook.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mediaplayer.com;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.mediaplayer.utility.FBUtils;
import com.mediaplayer.utility.StaticMusic;

public class LoginUsingActivityActivity extends Activity {

	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private UiLifecycleHelper uiHelper;
	OpenRequest req;
	Session session;
	SongInfo songInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Log.i("FB", "ENTERED ACTIVITY");
		uiHelper = new UiLifecycleHelper(this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		songInfo = (SongInfo) getIntent().getSerializableExtra("song_info");
		req = new OpenRequest(LoginUsingActivityActivity.this);
		req.setPermissions(Arrays.asList("publish_actions,publish_stream"));
		try{
			FBUtils.getSessionInfo().openForPublish(
					req.setCallback(statusCallback).setRequestCode(100));
		}catch(Exception e){
			FBUtils.setSessionInfo(Session.openActiveSession(this, true, statusCallback) );
			FBUtils.getSessionInfo().openForPublish(
					req.setCallback(statusCallback).setRequestCode(100));
		}
		Log.i("FB", "OVER ACTIVITY");
		uiHelper.onResume();

	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// TODO Auto-generated method stub
		if (state.isOpened()) {
			FBUtils.publishStory(session, songInfo, this);
			finish();
		} else if (state.isClosed()) {
			try{
				FBUtils.getSessionInfo().openForPublish(
						req.setCallback(statusCallback).setRequestCode(100));
			}catch(Exception e){
				Session.openActiveSession(this, false, statusCallback);
			}
			Log.i("FB ACTIVITY", "Logged out...");
		}

	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);

		}

	}

}
