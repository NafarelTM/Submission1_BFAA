package com.dicoding.submissionfundamental1.database

import android.database.Cursor
import com.dicoding.submissionfundamental1.model.User

object CursorHelper {
    fun toArrayList(cursor: Cursor?): ArrayList<User>{
        val listFavorite = ArrayList<User>()
        if(cursor != null){
            while (cursor.moveToNext()){
                val avatar = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavColumns.AVATAR))

                val userFav = User()
                userFav.avatar = avatar
                listFavorite.add(userFav)
            }
        }

        return listFavorite
    }
}