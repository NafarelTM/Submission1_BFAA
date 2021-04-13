package com.dicoding.submissionfundamental1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionfundamental1.User
import com.dicoding.submissionfundamental1.database.FavoriteDatabase
import com.dicoding.submissionfundamental1.database.UserFavorite
import com.dicoding.submissionfundamental1.database.UserFavoriteDao

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var favoriteDatabase: FavoriteDatabase? = FavoriteDatabase.getDatabase(application)
    private var favoriteDao: UserFavoriteDao? = favoriteDatabase?.userFavoriteDao()

    fun getFavorite(): LiveData<List<UserFavorite>>? {
        return favoriteDao?.queryAllFav()
    }

}