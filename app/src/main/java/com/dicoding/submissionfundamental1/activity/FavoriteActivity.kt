package com.dicoding.submissionfundamental1.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.model.User
import com.dicoding.submissionfundamental1.adapter.ListUserAdapter
import com.dicoding.submissionfundamental1.model.UserFavorite
import com.dicoding.submissionfundamental1.databinding.ActivityFavoriteBinding
import com.dicoding.submissionfundamental1.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var listAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAdapter = ListUserAdapter()
        listAdapter.notifyDataSetChanged()
        binding.apply {
            rvFavorite.setHasFixedSize(true)
            val divider = DividerItemDecoration(rvFavorite.context, DividerItemDecoration.VERTICAL)
            rvFavorite.addItemDecoration(divider)
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.adapter = listAdapter
        }
        listAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        viewModel.getFavorite()?.observe(this, {
            val listUser: ArrayList<User>
            if (it.isNotEmpty()) {
                listUser = toArrayList(it)
                listAdapter.setData(listUser)
                showLoading(false)
            } else {
                listUser = toArrayList(it)
                listAdapter.setData(listUser)
                binding.txtZeroUser.visibility = View.VISIBLE
                showLoading(false)
            }
        })

        binding.btnBack.setOnClickListener {
            finish()
        }

        if(!isConnected(this)) showErrorDialog()
    }

    private fun showSelectedUser(data: User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.apply {
            putExtra(ProfileActivity.EXTRA_USER_ID, data.id)
            putExtra(ProfileActivity.EXTRA_USER, data.username)
            putExtra(ProfileActivity.EXTRA_USER_AVATAR, data.avatar)
            putExtra(ProfileActivity.EXTRA_USER_LINK, data.githubLink)
        }
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun toArrayList(list: List<UserFavorite>): ArrayList<User> {
        val listFavUser = ArrayList<User>()
        for(favUser in list){
            val user = User()
            user.id = favUser.id
            user.username = favUser.username
            user.avatar = favUser.avatar
            user.githubLink = favUser.github_link

            listFavUser.add(user)
        }
        return listFavUser
    }

    @SuppressLint("NewApi")
    private fun isConnected(favoriteActivity: FavoriteActivity): Boolean{
        val connectivityManager: ConnectivityManager = favoriteActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private fun showErrorDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.disconnect)
            .setCancelable(false)
            .setPositiveButton(R.string.to_connect) { _: DialogInterface, _: Int ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton(R.string.to_cancel){ dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}