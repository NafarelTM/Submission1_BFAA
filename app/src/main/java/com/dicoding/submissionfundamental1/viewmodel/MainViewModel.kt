package com.dicoding.submissionfundamental1.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel : ViewModel() {

    private val listLiveData = MutableLiveData<ArrayList<User>>()

    fun setSearchResult(username: String, context: Context) {
        val list = ArrayList<User>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: JSONObject
            ) {
                try {
                    val dataArray = responseBody.getJSONArray("items")
                    for (i in 0 until dataArray.length()) {
                        val dataObject = dataArray.getJSONObject(i)
                        val user = User()
                        user.id = dataObject.getInt("id")
                        user.username = dataObject.getString("login")
                        user.avatar = dataObject.getString("avatar_url")
                        user.githubLink = dataObject.getString("html_url")
                        list.add(user)
                    }

                    listLiveData.postValue(list)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                throwable: Throwable,
                errorResponse: JSONArray
            ) {
                val errorMsg = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : $errorResponse"
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun randomList(context: Context) {
        val listRandom = ArrayList<User>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                response: JSONArray
            ) {
                try {
                    for (i in 0 until response.length()) {
                        val dataObject = response.getJSONObject(i)
                        val user = User()
                        user.id = dataObject.getInt("id")
                        user.username = dataObject.getString("login")
                        user.avatar = dataObject.getString("avatar_url")
                        user.githubLink = dataObject.getString("html_url")
                        listRandom.add(user)
                    }

                    listLiveData.postValue(listRandom)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                throwable: Throwable?,
                errorResponse: JSONArray?
            ) {
                Toast.makeText(context, R.string.data_not_found, Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getSearchResult(): LiveData<ArrayList<User>> {
        return listLiveData
    }
}
