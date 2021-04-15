package com.dicoding.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.dicoding.submissionfundamental1"
    const val SCHEME = "content"

    class FavColumns: BaseColumns{
        companion object{
            private const val TABLE_NAME = "user_favorite"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val GITHUB_LINK = "github_link"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }

}