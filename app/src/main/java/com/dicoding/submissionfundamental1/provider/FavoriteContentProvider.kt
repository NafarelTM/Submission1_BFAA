package com.dicoding.submissionfundamental1.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.submissionfundamental1.database.FavoriteDatabase
import com.dicoding.submissionfundamental1.database.UserFavoriteDao

class FavoriteContentProvider : ContentProvider() {

    private lateinit var favoriteDao: UserFavoriteDao

    companion object{
        const val FAVORITE_ID = 1
        private const val AUTHORITY = "com.dicoding.submissionfundamental1"
        private const val TABLE_NAME = "user_favorite"
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        favoriteDao = context?.let { FavoriteDatabase.getDatabase(it)?.userFavoriteDao() }!!
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val cursor: Cursor?
        if(uriMatcher.match(uri) == FAVORITE_ID){
            cursor = favoriteDao.queryAllCursor()
            if (context != null){
                cursor.setNotificationUri(context?.contentResolver, uri)
            }
        } else{
            cursor = null
        }

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}