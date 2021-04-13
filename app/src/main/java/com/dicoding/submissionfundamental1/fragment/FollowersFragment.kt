package com.dicoding.submissionfundamental1.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.User
import com.dicoding.submissionfundamental1.activity.ProfileActivity
import com.dicoding.submissionfundamental1.adapter.ListUserAdapter
import com.dicoding.submissionfundamental1.databinding.FragmentFollowsBinding
import com.dicoding.submissionfundamental1.viewmodel.FollowersViewModel

class FollowersFragment: Fragment(R.layout.fragment_follows) {

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FollowersViewModel
    private lateinit var listAdapter: ListUserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ProfileActivity.EXTRA_USER).toString()
        _binding = FragmentFollowsBinding.bind(view)

        showLoading(true)
        listAdapter = ListUserAdapter()
        listAdapter.notifyDataSetChanged()
        binding?.apply {
            rvFollows.setHasFixedSize(true)
            val divider = DividerItemDecoration(rvFollows.context, DividerItemDecoration.VERTICAL)
            rvFollows.addItemDecoration(divider)
            rvFollows.layoutManager = LinearLayoutManager(activity)
            rvFollows.adapter = listAdapter
        }
        listAdapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        viewModel.setFollowers(username, context)
        viewModel.getFollowers().observe(viewLifecycleOwner, {
            if (it != null){
                listAdapter.setData(it)
                showLoading(false)
            }
        })

        if(!isConnected(activity)) showErrorDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSelectedUser(data: User) {
        val intent = Intent(context, ProfileActivity::class.java)
        intent.putExtra(ProfileActivity.EXTRA_USER, data.username)
        startActivity(intent)
    }

    private fun showLoading(state: Boolean){
        if(state){
            binding?.progressBar?.visibility = View.VISIBLE
        } else{
            binding?.progressBar?.visibility = View.GONE
        }
    }

    private fun showErrorDialog() {
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        builder?.setMessage(R.string.disconnect)
                ?.setCancelable(false)
                ?.setPositiveButton(R.string.to_connect) { _: DialogInterface, _: Int ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                ?.setNegativeButton(R.string.to_cancel){ dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }
        val alert: AlertDialog? = builder?.create()
        alert?.show()
    }

    @SuppressLint("NewApi")
    private fun isConnected(activity: FragmentActivity?): Boolean{
        val connectivityManager: ConnectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

}