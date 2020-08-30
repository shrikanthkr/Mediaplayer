package com.em.mediaplayer.player

import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.api.PendingResult

interface Command {
    fun execute(): PendingResult<RemoteMediaClient.MediaChannelResult>
}