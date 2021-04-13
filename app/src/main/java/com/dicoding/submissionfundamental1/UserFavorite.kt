package com.dicoding.submissionfundamental1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_favorite")
data class UserFavorite(
    @PrimaryKey
    val id: Int,
    val username: String
) : Serializable