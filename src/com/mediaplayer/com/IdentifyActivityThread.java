package com.mediaplayer.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;
import com.mediaplayer.listener.ProgressUpdateIdentifyThread;
import com.mediaplayer.manager.EchonestApiManager;
import com.mediaplayer.utility.CheapSoundFile;
import com.mediaplayer.utility.Util;

public class IdentifyActivityThread  extends AsyncTask<String,Void,Track>{
	File file;
	Track track;
	String path;
	EchoNestAPI echonestApi;
	Util util;
	String msg;
	EchonestApiManager.EchonestApiListener listener;

	public IdentifyActivityThread(String path, EchonestApiManager.EchonestApiListener listener) {
		this.path = path;
		util = new Util();
		msg = "";
		this.listener = listener;
	}

	@Override
	protected Track doInBackground(String... strings) {
		try {
			CheapSoundFile soundFile;
			JSONObject jsonObj;
			soundFile = CheapSoundFile.create(path);
			int endFrame = (int) (1.0 * 30 * soundFile.getSampleRate()
					/ soundFile.getSamplesPerFrame() + 0.5);
			String outPath = util.makeRingtoneFilename("toupload",
					util.getExtensionFromFilename(path));
			file = new File(outPath);
			if (file.exists())
				file.delete();

			soundFile.WriteFile(file, 0, endFrame);
			Log.i("PATH OF FILE", file.getAbsolutePath());
			echonestApi = new EchoNestAPI();
			track = echonestApi.uploadTrack(file,true);
			file.delete();
			jsonObj = new JSONObject(track.toString());
			Log.d("Track Details", track.toString() + "");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (EchoNestException e1) {
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return track;
	}

	@Override
	protected void onPostExecute(Track track) {
		listener.onResult(track);
		super.onPostExecute(track);
	}
}
