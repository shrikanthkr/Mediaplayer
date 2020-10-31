package com.em.mediaplayer.ui.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.em.mediaplayer.app.MediaApplication
import com.em.mediaplayer.player.PlayerController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview




@ExperimentalStdlibApi
class CallReceiver : BroadcastReceiver() {

    lateinit var playerController: PlayerController

    init {
        Log.d(TAG, "init")
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val component = (context.applicationContext as MediaApplication).appComponent
        playerController = component.controller()
        intent?.run {
            if (this.action == "android.intent.action.PHONE_STATE") {
                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                if (state == TelephonyManager.EXTRA_STATE_RINGING
                        || state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                    playerController.pauseBy(ACCESSOR_ID)
                } else {
                    playerController.resumeBy(ACCESSOR_ID)
                }
            }
        }
    }


    companion object {
        const val TAG = "CallReceiver"
        const val ACCESSOR_ID = 11
    }
}
