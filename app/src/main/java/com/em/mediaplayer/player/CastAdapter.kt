package com.em.mediaplayer.player

import android.util.Log
import com.em.mediaplayer.app.server.FileServer
import com.em.repository.Song
import com.google.android.gms.cast.*
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.w3c.dom.Comment
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class CastAdapter(
        private val server: FileServer,
        private val scope: CoroutineScope,
        session: CastSession,
        private val singleThreadDispatcher: CoroutineDispatcher
) : PlayerAdapter() {

    private val remoteMediaClient = session.remoteMediaClient
    private val progressListener = RemoteMediaClient.ProgressListener { current, _ ->
        if (remoteMediaClient.mediaStatus != null && remoteMediaClient.mediaStatus.playerState == MediaStatus.PLAYER_STATE_PLAYING) {
            dispatchProgress(current)
        }
    }

    private val remoteMediaCallback = object : RemoteMediaClient.Callback() {
        override fun onStatusUpdated() {
            super.onStatusUpdated()
            if (remoteMediaClient.mediaStatus != null) {
                Log.d(TAG, " ${remoteMediaClient.mediaStatus.playerState}")
                when (remoteMediaClient.mediaStatus.playerState) {
                    MediaStatus.PLAYER_STATE_UNKNOWN -> dispatchError()
                    MediaStatus.PLAYER_STATE_IDLE -> {
                        Log.d(TAG, " Idle ${remoteMediaClient.mediaStatus.idleReason}")
                        when (remoteMediaClient.mediaStatus.idleReason) {
                            MediaStatus.IDLE_REASON_FINISHED -> {
                                dispatchEnd()
                            }
                            MediaStatus.IDLE_REASON_ERROR, MediaStatus.IDLE_REASON_INTERRUPTED -> {
                                dispatchError()
                            }
                        }
                    }
                    MediaStatus.PLAYER_STATE_PLAYING -> {
                        remoteMediaClient.addProgressListener(progressListener, 500)
                        dispatchStart()
                    }
                    MediaStatus.PLAYER_STATE_PAUSED -> {
                        remoteMediaClient.removeProgressListener(progressListener)
                        dispatchPause()
                    }
                    MediaStatus.PLAYER_STATE_BUFFERING, MediaStatus.PLAYER_STATE_LOADING -> {
                        dispatchLoading()
                    }

                }
            }
        }

    }

    private val commandChannel = Channel<Command>(Channel.RENDEZVOUS)

    companion object {
        const val TAG = "CastAdapter"
    }

    init {
        remoteMediaClient.registerCallback(remoteMediaCallback)
        scope.launch(singleThreadDispatcher) {
            for(command in commandChannel){
                val result = withContext(Dispatchers.Main){
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


    override fun play(song: Song) {
        if (!server.isAlive) {
            server.start()
        }
        server.serve(song.uri)
        Log.d(TAG, "Song Duration on play: ${song.duration}")
        val metaData = MediaMetadata(MediaMetadata.MEDIA_TYPE_MUSIC_TRACK)
        //val url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/mp4/DesigningForGoogleCast.mp4"
        val url = "http://${server.ip}/${song.id}.mp3"
        val info = MediaInfo.Builder(url)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("audio/mpeg")
                .setMetadata(metaData)
                .setStreamDuration(song.duration)
                .build()
        val load = MediaLoadRequestData.Builder()
                .setMediaInfo(info)
                .setAutoplay(true)
                .build()
        executeCommand {
            remoteMediaClient.load(load)
        }
    }

    override fun pause() {
        executeCommand {
            remoteMediaClient.pause()
        }
    }

    override fun resume() {
        executeCommand {
            remoteMediaClient.play()
        }
    }

    override fun seek(position: Long) {
        val seekOption = MediaSeekOptions.Builder()
                .setPosition(position)
                .setIsSeekToInfinite(false)
                .setResumeState(MediaSeekOptions.RESUME_STATE_UNCHANGED)
                .build()
        Log.d(TAG, "Song Duration on Seek: $position")
        executeCommand {
            remoteMediaClient.seek(seekOption)
        }
    }

    override fun clear() {
        server.stop()
        remoteMediaClient.stop()
        remoteMediaClient.registerCallback(remoteMediaCallback)
        remoteMediaClient.removeProgressListener(progressListener)
    }


    private fun executeCommand(command: () -> PendingResult<RemoteMediaClient.MediaChannelResult>) {
        scope.launch(singleThreadDispatcher) {
            commandChannel.send(object : Command {
                override fun execute(): PendingResult<RemoteMediaClient.MediaChannelResult> {
                    return command()
                }
            })
        }
    }
}