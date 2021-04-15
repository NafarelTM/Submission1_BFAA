package com.dicoding.submissionfundamental1.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var id: Int? = 0,
    var username: String? = "",
    var name: String? = "",
    var location: String? = "",
    var repository: Int? = 0,
    var company: String? = "",
    var followers: Int? = 0,
    var following: Int? = 0,
    var avatar: String? = "",
    var githubLink: String? = ""
): Parcelable
