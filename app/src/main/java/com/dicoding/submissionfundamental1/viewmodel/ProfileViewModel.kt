package com.dicoding.submissionfundamental1.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionfundamental1.database.FavoriteDatabase
import com.dicoding.submissionfundamental1.User
import com.dicoding.submissionfundamental1.database.UserFavorite
import com.dicoding.submissionfundamental1.database.UserFavoriteDao
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    val listLiveData = MutableLiveData<User>()

    private var favoriteDatabase: FavoriteDatabase? = FavoriteDatabase.getDatabase(application)
    private var favoriteDao: UserFavoriteDao? = favoriteDatabase?.userFavoriteDao()

    fun setUserProfile(username: String) {
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val user = User()
                    user.id = jsonObject.getInt("id")
                    user.username = jsonObject.getString("login")
                    user.avatar = jsonObject.getString("avatar_url")
                    user.name = jsonObject.getString("name")
                    user.company = jsonObject.getString("company")
                    user.followers = jsonObject.getInt("followers")
                    user.following = jsonObject.getInt("following")
                    user.location = jsonObject.getString("location")
                    user.repository = jsonObject.getInt("public_repos")
                    user.githubLink = jsonObject.getString("html_url")

                    listLiveData.postValue(user)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("Profile View Model", error.message.toString())
            }

        })
    }

    fun getUserProfile(): LiveData<User> {
        return listLiveData
    }

    suspend fun countFav(id: Int) = favoriteDao?.countFav(id)

    fun insertFav(id: Int, username: String, avatar: String, githubLink: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFavorite(id, username, avatar, githubLink)
            favoriteDao?.insertFav(user)
        }
    }

    fun deleteFav(id: Int, username: String, avatar: String, githubLink: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFavorite(id, username, avatar, githubLink)
            favoriteDao?.deleteFav(user)
        }
    }

//    fun deleteFav(id: Int){
//        CoroutineScope(Dispatchers.IO).launch {
//            favoriteDao?.deleteFav(id)
//        }
//    }
}