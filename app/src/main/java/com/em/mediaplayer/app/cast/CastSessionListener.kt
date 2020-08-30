package com.em.mediaplayer.app.cast

import android.util.Log
import com.em.mediaplayer.app.cast.CastSessionListener.CastSessionStatus.*
import com.google.android.gms.cast.CastStatusCodes
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

@ExperimentalCoroutinesApi
class CastSessionListener : SessionManagerListener<CastSession> {
    private val _castSessionState = MutableStateFlow<CastSessionStatus?>(null)
    val castSessionState = _castSessionState.filterNotNull()

    override fun onSessionStarting(castSession: CastSession?) {
        Log.d(TAG, " On Cast Starting ")
    }

    override fun onSessionStartFailed(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Start Failed${CastStatusCodes.getStatusCodeString(p1)}")
        _castSessionState.value = Disconnected(castSession)
    }

    override fun onSessionStarted(castSession: CastSession?, p1: String?) {
        Log.d(TAG, " On Cast Session Started $p1")
        _castSessionState.value = Available(requireNotNull(castSession))
    }

    override fun onSessionResuming(castSession: CastSession?, p1: String?) {
        Log.d(TAG, " On Cast Resuming $p1")
    }

    override fun onSessionResumeFailed(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Resume Failed ${CastStatusCodes.getStatusCodeString(p1)}")
        _castSessionState.value = Disconnected(castSession)
    }

    override fun onSessionResumed(castSession: CastSession?, p1: Boolean) {
        Log.d(TAG, " On Cast Resumed $p1")
        _castSessionState.value = Resumed(requireNotNull(castSession), p1)
    }

    override fun onSessionSuspended(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Suspended ${CastStatusCodes.getStatusCodeString(p1)}")
    }

    override fun onSessionEnded(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Ended ${CastStatusCodes.getStatusCodeString(p1)}")
        _castSessionState.value = Disconnected(castSession)
    }

    override fun onSessionEnding(castSession: CastSession?) {
        Log.d(TAG, " On Cast Ending")
    }

    companion object {
        const val TAG = "CastSessionListener"
    }

    sealed class CastSessionStatus {
        class Available(val session: CastSession) : CastSessionStatus()
        class Resumed(val session: CastSession, val wasSuspended: Boolean) : CastSessionStatus()
        class Disconnected(val session: CastSession?) : CastSessionStatus()
    }
}