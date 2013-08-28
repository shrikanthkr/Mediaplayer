package com.mediaplayer.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.app.Activity;
import android.media.AudioFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class IdentifyActivity extends Activity {
	TextView identify_textview;
	ArrayList<HashMap<String, String>> songList;
	SongsManager pl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identified_xml);
		identify_textview = (TextView) findViewById(R.id.identify_textView);
		pl = new SongsManager();
		songList = pl.getPlayList();
		Log.i("Identify ", "playlist");
		HttpParams httpParams = new BasicHttpParams();
		HttpClient client = new DefaultHttpClient(httpParams);
		HttpPost httpost = new HttpPost(
				"http://developer.echonest.com/api/v4/track/upload?api_key=WH8YIHGZUX1XVLISU&filetype=mp3");
		File file = new File(songList.get(0).get("songPath"));
		/*MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "binary/octet-stream");
		httpost.setEntity(mpEntity);
		mpEntity.addPart("name", cbFile);
		httpost.setHeader("Content-type", "application/octet-stream");
		httpost.setEntity(mpEntity);
		HttpResponse response = null;
		try {
			Log.i("Identify", "Executinb");
			response = client.execute(httpost);
			InputStream instream = response.getEntity().getContent();
			String result = convertStreamToString(instream);
			Log.i("Identify", result);
			identify_textview.setText(result);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("Identify", "Exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i("Identify", "Exception");
			e.printStackTrace();
		}
	}

	private String convertStreamToString(InputStream instream) {
		// TODO Auto-generated method stub
		BufferedReader r = new BufferedReader(new InputStreamReader(instream));
		String x = "";
		try {
			x = r.readLine();
			String total = "";
			while (x != null) {
				total += x;
				x = r.readLine();
				return total;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	}
}
