package com.em.mediaplayer.player

import android.app.Application
import com.em.mediaplayer.app.cast.CastSessionListener
import com.em.mediaplayer.app.cast.CastSessionListener.CastSessionStatus.Available
import com.em.mediaplayer.app.cast.CastSessionListener.CastSessionStatus.Resumed
import com.em.mediaplayer.app.di.qualifiers.SingleThreadDispatcher
import com.em.mediaplayer.app.server.FileServer
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class AdapterPrioritizer @Inject constructor(
        application: Application,
        playerController: PlayerController,
        scope: CoroutineScope,
        server: FileServer,
        @SingleThreadDispatcher
        private val singleThreadDispatcher: CoroutineDispatcher,
        private val defaultAdapter: PlayerAdapter
) {
    private val castSessionListener = CastSessionListener()


    init {
        val castContext = CastContext.getSharedInstance(application)
        val sessionManager = castContext.sessionManager
        playerController.switchAdapter(defaultAdapter)
        sessionManager.addSessionManagerListener(castSessionListener, CastSession::class.java)

        scope.launch(Dispatchers.Main) {
            castSessionListener.castSessionState.collect {
                when (it) {
                    is Available -> {
                        playerController.switchAdapter(CastAdapter(server, scope, it.session, singleThreadDispatcher))
                    }
                    is Resumed -> {
                        if (!it.wasSuspended) {
                            playerController.switchAdapter(CastAdapter(server, scope, it.session, singleThreadDispatcher))
                        }
                    }
                    else -> playerController.switchAdapter(defaultAdapter)
                }
            }
        }
    }
}