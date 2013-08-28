package com.mediaplayer.utility;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.mediaplayer.com.R;
import com.mediaplayer.com.SongInfo;

public class FBUtils {

	public static Session getSessionInfo(){
		return  Session.getActiveSession();

	}
	public static void setSessionInfo(Session session){
		Session.setActiveSession(session);

	}

	public static void publishStory(Session session,SongInfo songInfo,final Activity activity) {
		session = Session.getActiveSession();

		if (session != null){

			Bundle postParams = new Bundle();
			postParams.putString("name", "Hearing Now : "+songInfo.getTitle() +"  #myappname");
			postParams.putString("caption", "Shared from myapp .");
			postParams.putString("message", "Hearing To :"+songInfo.getTitle() +" #myappname");
			postParams.putString("description", "Shared from myapp.");


			Request.Callback callback= new Request.Callback() {

				@Override
				public void onCompleted(Response response) {
					// TODO Auto-generated method stub

					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(activity,
								error.getErrorMessage(),
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(activity,
								"Shared",
								Toast.LENGTH_SHORT).show();
					}
					Log.i("response", response.toString());
				}
			};

			Request request = new Request(session, "me/feed", postParams, 
					HttpMethod.POST, callback);
			Log.i("FB ACTIVITY", "Starting Async upload");
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

}
