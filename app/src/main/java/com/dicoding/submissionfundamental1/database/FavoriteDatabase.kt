package com.dicoding.submissionfundamental1.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.submissionfundamental1.model.UserFavorite

@Database(
    entities = [UserFavorite::class],
    version = 1
)

abstract class FavoriteDatabase : RoomDatabase() {
    companion object {
        var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context): FavoriteDatabase? {
            if (INSTANCE == null) {
                synchronized(FavoriteDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDatabase::class.java,
                        "favorite_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun userFavoriteDao(): UserFavoriteDao
}