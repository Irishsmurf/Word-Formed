package com.paddez.wordformed

import androidx.room.*

@Dao
interface HiScoreDao {
    @Query("SELECT * FROM hiscores ORDER BY score DESC LIMIT 25")
    suspend fun getAllHiScores(): List<HiScore>

    @Insert
    suspend fun insert(hiScore: HiScore): Long

    @Delete
    suspend fun delete(hiScore: HiScore)
}
