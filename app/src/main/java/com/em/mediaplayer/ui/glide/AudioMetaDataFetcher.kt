package com.em.mediaplayer.ui.glide

import android.content.ContentUris
import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream


class AudioMetaDataFetcher(private val context: Context, private val model: AudioCover) : DataFetcher<InputStream> {

    private var stream: InputStream? = null
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, model.path)
            val picture = retriever.embeddedPicture
            if (picture != null) {
                stream = ByteArrayInputStream(picture)
                callback.onDataReady(ByteArrayInputStream(picture))
            } else {
                throw Resources.NotFoundException()
            }
        } catch (t: Exception) {
            callback.onLoadFailed(t)
        } finally {
            retriever.release()
        }
    }

    override fun cleanup() {
        try {
            stream?.close()
        } catch (exception: IOException) {
            //ignore
        }
    }

    override fun cancel() = Unit

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }

}