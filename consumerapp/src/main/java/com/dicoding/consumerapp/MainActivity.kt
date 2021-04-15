package com.dicoding.consumerapp

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
import com.dicoding.consumerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var listAdapter: ListUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAdapter = ListUserAdapter()
        listAdapter.notifyDataSetChanged()
        binding.apply {
            rvUser.setHasFixedSize(true)
            val divider = DividerItemDecoration(rvUser.context, DividerItemDecoration.VERTICAL)
            rvUser.addItemDecoration(divider)
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.adapter = listAdapter
        }

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.setFavorite(this)
        viewModel.getFavorite().observe(this, {
            if (it.isNotEmpty()) {
                listAdapter.setData(it)
                showLoading(false)
            } else {
                listAdapter.setData(it)
                binding.txtZeroUser.visibility = View.VISIBLE
                showLoading(false)
            }
        })

        if (!isConnected(this)) showErrorDialog()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    @SuppressLint("NewApi")
    private fun isConnected(activity: MainActivity): Boolean {
        val connectivityManager: ConnectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private fun showErrorDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Please connect to the internet first")
            .setCancelable(false)
            .setPositiveButton("Connect") { _: DialogInterface, _: Int ->
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}