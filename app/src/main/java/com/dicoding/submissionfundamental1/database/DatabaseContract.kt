package com.dicoding.submissionfundamental1.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.submissionfundamental1"
    const val SCHEME = "content"

    class FavColumns: BaseColumns{
        companion object{
            private const val TABLE_NAME = "user_favorite"
            const val AVATAR = "avatar"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

}