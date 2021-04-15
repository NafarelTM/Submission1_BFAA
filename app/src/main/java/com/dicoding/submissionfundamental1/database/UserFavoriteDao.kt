package com.dicoding.submissionfundamental1.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.submissionfundamental1.model.UserFavorite

@Dao
interface UserFavoriteDao {
    @Insert
    suspend fun insertFav(userFavorite: UserFavorite)

    @Delete
    suspend fun deleteFav(userFavorite: UserFavorite)

    @Query("SELECT * FROM user_favorite")
    fun queryAllFav(): LiveData<List<UserFavorite>>

    @Query("SELECT count(*) FROM user_favorite WHERE id = :id")
    suspend fun countFav(id: Int): Int

    @Query("SELECT * FROM user_favorite")
    fun queryAllCursor(): Cursor
}