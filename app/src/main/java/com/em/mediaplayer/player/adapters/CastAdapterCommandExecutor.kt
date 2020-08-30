package com.em.mediaplayer.player.adapters

import android.util.Log
import com.em.mediaplayer.app.di.qualifiers.SingleThreadDispatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class CastAdapterCommandExecutor @Inject constructor(
        private val scope: CoroutineScope,
        @SingleThreadDispatcher
        private val singleThreadDispatcher: CoroutineDispatcher
) {
    private val commandChannel = Channel<CastAdapterCommand>(Channel.RENDEZVOUS)


    init {
        scope.launch(singleThreadDispatcher) {
            for (command in commandChannel) {
                val result = withContext(Dispatchers.Main) {
                    command.execute()
                }
                Log.d(TAG, "${Thread.currentThread()} EXECUTING $command")
                suspendCoroutine<Unit> {
                    result.addStatusListener { status ->
                        Log.d(TAG, "${Thread.currentThread()}  EXECUTED $command, $status")
                        it.resume(Unit)
                    }
                }
            }
        }
    }

    fun execute(command: CastAdapterCommand) {
        scope.launch(singleThreadDispatcher) {
            commandChannel.send(command)
        }
    }

    companion object {
        const val TAG = "CastAdapterCommandExecutor"
    }
}