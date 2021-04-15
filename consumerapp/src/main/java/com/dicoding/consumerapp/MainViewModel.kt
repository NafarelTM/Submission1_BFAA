package com.dicoding.consumerapp

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application): AndroidViewModel(application) {

    private var listFav = MutableLiveData<ArrayList<UserFavorite>>()

    fun setFavorite(context: Context){
        val cursor = context.contentResolver.query(
            DatabaseContract.FavColumns.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        val list = CursorHelper.toArrayList(cursor)
        listFav.postValue(list)
    }

    fun getFavorite(): LiveData<ArrayList<UserFavorite>> {
        return listFav
    }

}