package com.mediaplayer.com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Track;
import com.mediaplayer.listener.ProgressUpdateIdentifyThread;
import com.mediaplayer.utility.CheapSoundFile;
import com.mediaplayer.utility.SongsHolder;
import com.mediaplayer.utility.Util;

public class IdentifyActivityThread  {/*
	public File file;
	UpdateListener listener;
	Track track;
	String path;
	EchoNestAPI echonestApi;
	Util util;
	Activity activity;
	String msg;
	ProgressUpdateIdentifyThread progressUpdateThreadListener;
	FakeUpdateThread fakeThread;

	public IdentifyActivityThread(String path, Activity activity,
			UpdateListener listener,
			ProgressUpdateIdentifyThread progressUpdateThreadListener) {
		this.listener = listener;
		this.path = path;
		util = new Util();
		this.activity = activity;
		msg = "";
		this.progressUpdateThreadListener = progressUpdateThreadListener;
	}




	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		progressUpdateThreadListener.onProgressChangeUpdate(0);
		CheapSoundFile soundFile;
		try {
			soundFile = CheapSoundFile.create(path);

			//Log.i("IDENTIFY", "SIZE:" + soundFile.getFileSizeBytes());
			int endFrame = (int) (1.0 * 30 * soundFile.getSampleRate()
					/ soundFile.getSamplesPerFrame() + 0.5);
			String outPath = util.makeRingtoneFilename("toupload",
					util.getExtensionFromFilename(path));
			File outputFile = new File(outPath);
			if (outputFile.exists())
				outputFile.delete();

			soundFile.WriteFile(outputFile, 0, endFrame);
			file = new File(outputFile.getAbsolutePath());
			//Log.i("PATH OF FILE", file.getAbsolutePath());
			echonestApi = new EchoNestAPI("WH8YIHGZUX1XVLISU");
			progressUpdateThreadListener.onProgressChangeUpdate(25);
			fakeThread = new FakeUpdateThread();
			fakeThread.start();
			track = echonestApi.uploadTrack(file);
			progressUpdateThreadListener.onProgressChangeUpdate(75);
			fakeThread.setProgress(75);
			file.delete();
			track.waitForAnalysis(200000);
			progressUpdateThreadListener.onProgressChangeUpdate(100);
			final JSONObject jsonObj = new JSONObject(track.toString());
			//Log.i("IDENTIFY", track.toString());
			if (path.equals(SongsHolder.songInfo.getData())) {
				if (jsonObj.has("title")) {
					SongsHolder.songInfo.setTitle(track.getTitle());
					msg += "Title : " + SongsHolder.songInfo.getTitle() + "\n";
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								if (jsonObj.has("release")) {
									SongsHolder.songInfo.setAlbum(track
											.getReleaseName());
									msg += "Album : "
											+ SongsHolder.songInfo.getAlbum()
											+ "\n";
								} else {
									msg += "Album : " + "";
								}
								if (jsonObj.has("artist")) {
									SongsHolder.songInfo.setArtist(track
											.getArtistName());
									msg += "Artist : "
											+ SongsHolder.songInfo.getArtist()
											+ "\n";
								} else {

								}
								new ChoiceDialog(activity,
										new DialogChangeListener() {

									@Override
									public void receivedChange(
											boolean changed) {
										// TODO Auto-generated method
										// stub

										if (changed) {
											Util.updateTrack(
													activity,
													SongsHolder.songInfo);
											//Log.i("ID", "FAKE THREAD ID ::"+fakeThread.getId());
											listener.onUpdate(true);
										}
										else{
											progressUpdateThreadListener.onProgressChangeUpdate(100);
										}
									}
								}, msg).show();

							} catch (EchoNestException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								listener.onUpdate(false);
							}
						}
					});
				} else {
					listener.onUpdate(false);
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			listener.onUpdate(false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (EchoNestException e1) {
			// TODO Auto-generated catch block
			listener.onUpdate(false);
			e1.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			listener.onUpdate(false);
		} finally {
			progressUpdateThreadListener.onProgressChangeUpdate(100);
			try {
				file.delete();

			} catch (Exception e) {
			}
		}

		return;
	}


	public interface DialogChangeListener {

		public void receivedChange(boolean changed);
	}

	public class FakeUpdateThread extends Thread {

		int progress = 26;


		*//**
		 * @return the progress
		 *//*
		public int getProgress() {
			return progress;
		}


		*//**
		 * @param progress
		 *            the progress to set
		 *//*
		public void setProgress(int progress) {
			this.progress = progress;
		}

		*//*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 *//*
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (progress < 75 ) {
				//Log.i("FAKE THREAD", "::::"+this.getId());
				progress += 1;
				try {
					Thread.sleep(600);
					if (path.equals(SongsHolder.songInfo.getData()))
						progressUpdateThreadListener
						.onProgressChangeUpdate(progress);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			progress=0;
			//Log.i("FAKE THREAD", "::::"+this.getId() +":: OVER") ;

			return;
		}

	}
*/
}
