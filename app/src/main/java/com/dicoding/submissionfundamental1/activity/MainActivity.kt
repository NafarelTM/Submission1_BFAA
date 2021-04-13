package com.dicoding.submissionfundamental1.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.User
import com.dicoding.submissionfundamental1.adapter.ListUserAdapter
import com.dicoding.submissionfundamental1.databinding.ActivityMainBinding
import com.dicoding.submissionfundamental1.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var listUserAdapter: ListUserAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.rvUser.setHasFixedSize(true)
        val divider = DividerItemDecoration(binding.rvUser.context, DividerItemDecoration.VERTICAL)
        binding.rvUser.addItemDecoration(divider)
        showList()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        viewModel.getSearchResult().observe(this, {
            if (it.size > 0) {
                listUserAdapter.setData(it)
                showLoading(false)
            } else {
                listUserAdapter.setData(it)
                Toast.makeText(this, R.string.not_found, Toast.LENGTH_SHORT).show()
                binding.txtZeroUser.visibility = View.VISIBLE
                showLoading(false)
            }
        })

        if (savedInstanceState == null) {
            viewModel.randomList(this)
        }

        if (!isConnected(this)) showErrorDialog()
        search()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val favIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(favIntent)
            }

            R.id.setting_menu -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showErrorDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.disconnect)
            .setCancelable(false)
            .setPositiveButton(R.string.to_connect) { _: DialogInterface, _: Int ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton(R.string.to_cancel) { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    @SuppressLint("NewApi")
    private fun isConnected(mainActivity: MainActivity): Boolean {
        val connectivityManager: ConnectivityManager =
            mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun search() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    if (!isConnected(this@MainActivity)) showErrorDialog()
                    binding.txtSuggestion.visibility = View.GONE
                    binding.txtZeroUser.visibility = View.GONE
                    viewModel.setSearchResult(query, this@MainActivity)
                    showLoading(true)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    if (!isConnected(this@MainActivity)) showErrorDialog()
                    binding.txtSuggestion.visibility = View.VISIBLE
                    binding.txtZeroUser.visibility = View.GONE
                    viewModel.randomList(this@MainActivity)
                    showLoading(true)
                }
                return false
            }
        })
    }

    private fun showList() {
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        listUserAdapter = ListUserAdapter()
        binding.rvUser.adapter = listUserAdapter
        listUserAdapter.notifyDataSetChanged()

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.apply {
            putExtra(ProfileActivity.EXTRA_USER_ID, user.id)
            putExtra(ProfileActivity.EXTRA_USER, user.username)
            putExtra(ProfileActivity.EXTRA_USER_AVATAR, user.avatar)
            putExtra(ProfileActivity.EXTRA_USER_LINK, user.githubLink)
        }
        startActivity(intent)
    }
}