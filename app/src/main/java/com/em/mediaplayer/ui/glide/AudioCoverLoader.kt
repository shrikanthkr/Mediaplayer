package com.em.mediaplayer.ui.glide

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import java.io.InputStream


class AudioCoverLoader(private val context: Context) : ModelLoader<AudioCover, InputStream> {
    override fun buildLoadData(model: AudioCover, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model), AudioMetaDataFetcher(context, model))
    }

    override fun handles(model: AudioCover): Boolean {
        return true
    }

    internal class Factory(private val context: Context) : ModelLoaderFactory<AudioCover, InputStream> {
        override fun teardown() = Unit
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AudioCover, InputStream> {
            return AudioCoverLoader(context)
        }
    }
}