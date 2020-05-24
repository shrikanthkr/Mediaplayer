package com.em.mediaplayer.app.cast

import android.util.Log
import com.em.mediaplayer.app.cast.CastSessionListener.CastSessionStatus.Available
import com.em.mediaplayer.app.cast.CastSessionListener.CastSessionStatus.Disconnected
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

@ExperimentalCoroutinesApi
class CastSessionListener : SessionManagerListener<CastSession> {
    private val _castSessionState = MutableStateFlow<CastSessionStatus?>(null)
    val castSessionState = _castSessionState.filterNotNull()


    //Caster ovverides

    override fun onSessionStarting(castSession: CastSession?) {
        Log.d(TAG, " On Cast Starting")
    }

    override fun onSessionStartFailed(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Casst Start Failed")
        _castSessionState.value = Disconnected(castSession)
    }

    override fun onSessionStarted(castSession: CastSession?, p1: String?) {
        Log.d(TAG, " On Cast Session Started")
        _castSessionState.value = Available(requireNotNull(castSession))
    }

    override fun onSessionResuming(castSession: CastSession?, p1: String?) {
        Log.d(TAG, " On Cast Resuming")
    }

    override fun onSessionResumeFailed(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Resume Failed")
        _castSessionState.value = Disconnected(castSession)
    }

    override fun onSessionResumed(castSession: CastSession?, p1: Boolean) {
        Log.d(TAG, " On Cast Resumed")
    }

    override fun onSessionSuspended(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Suspended")
        _castSessionState.value = Disconnected(castSession)
    }

    override fun onSessionEnded(castSession: CastSession?, p1: Int) {
        Log.d(TAG, " On Cast Ended")
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
        class Disconnected(val session: CastSession?) : CastSessionStatus()
    }
}