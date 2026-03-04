package com.paddez.wordformed

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hiscores")
data class HiScore(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val score: Int
) : Comparable<HiScore> {
    override fun compareTo(other: HiScore): Int {
        return other.score - score
    }

    override fun toString(): String {
        return name
    }
}
