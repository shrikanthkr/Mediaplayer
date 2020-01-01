package com.mediaplayer.repository

import androidx.room.Dao
import androidx.room.Query


@Dao
interface SongDao {
    /**
     * Counts the number of cheeses in the table.
     *
     * @return The number of cheeses.
     */
    @Query("SELECT COUNT(*) FROM " + "Hai")
    fun count(): Int
}