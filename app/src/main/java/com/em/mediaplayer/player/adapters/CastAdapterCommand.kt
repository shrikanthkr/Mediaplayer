package com.em.mediaplayer.player.adapters

import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.api.PendingResult

interface CastAdapterCommand {
    fun execute(): PendingResult<RemoteMediaClient.MediaChannelResult>
}