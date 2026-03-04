package com.paddez.wordformed

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HiScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hiScoreDao(): HiScoreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hiscores.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
