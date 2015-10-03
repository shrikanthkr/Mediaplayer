package com.mediaplayer.receiver;

import com.mediaplayer.utility.SongsHolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {

	static boolean wasPlaying=false;
	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		MyPhoneStateListener phoneListener=new MyPhoneStateListener();
		TelephonyManager telephony = (TelephonyManager) 
				context.getSystemService(Context.TELEPHONY_SERVICE);
		telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
	}

	public class MyPhoneStateListener extends PhoneStateListener {
		public void onCallStateChanged(int state,String incomingNumber){
			/*switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				try{
					if(!SongsHolder.music.isPlaying() && wasPlaying){
						SongsHolder.music.play();
						wasPlaying=false;
					}
				}catch(Exception e){

				}
				//Log.d("DEBUG", "IDLE");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				//Log.d("DEBUG", "OFFHOOK");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				try{
					if(SongsHolder.music.isPlaying()){
						SongsHolder.music.pause();
						wasPlaying=true;
					}
				}catch(Exception e){

				}
				//Log.d("DEBUG", "RINGING");
				break;
			}*/
		} 
	}
}
