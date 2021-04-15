package com.dicoding.submissionfundamental1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.submissionfundamental1.database.FavoriteDatabase
import com.dicoding.submissionfundamental1.model.UserFavorite
import com.dicoding.submissionfundamental1.database.UserFavoriteDao

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var favoriteDatabase: FavoriteDatabase? = FavoriteDatabase.getDatabase(application)
    private var favoriteDao: UserFavoriteDao? = favoriteDatabase?.userFavoriteDao()

    fun getFavorite(): LiveData<List<UserFavorite>>? {
        return favoriteDao?.queryAllFav()
    }

}