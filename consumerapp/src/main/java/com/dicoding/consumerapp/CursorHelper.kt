package com.dicoding.consumerapp

import android.database.Cursor

object CursorHelper {
    fun toArrayList(cursor: Cursor?): ArrayList<UserFavorite>{
        val listFavorite = ArrayList<UserFavorite>()
        if(cursor != null){
            while (cursor.moveToNext()){
                val username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.USERNAME))
                val avatar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))
                val githubLink = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.GITHUB_LINK))

                val userFav = UserFavorite(username, avatar, githubLink)
                listFavorite.add(userFav)
            }
        }

        return listFavorite
    }
}