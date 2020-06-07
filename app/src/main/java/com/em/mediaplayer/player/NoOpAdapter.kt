package com.em.mediaplayer.player

import com.em.repository.Song


class NoOpAdapter : PlayerAdapter() {

    override fun play(song: Song) = Unit

    override fun pause() = Unit

    override fun resume() = Unit

    override fun seek(position: Long) = Unit

    override fun clear() = Unit
}