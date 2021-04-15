package com.dicoding.submissionfundamental1.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submissionfundamental1.R
import com.dicoding.submissionfundamental1.adapter.SectionPagerAdapter
import com.dicoding.submissionfundamental1.databinding.ActivityProfileBinding
import com.dicoding.submissionfundamental1.viewmodel.ProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    companion object{
        const val EXTRA_USER_ID = "extra_user_id"
        const val EXTRA_USER = "extra_user"
        const val EXTRA_USER_AVATAR = "extra_user_avatar"
        const val EXTRA_USER_LINK = "extra_user_link"

        @StringRes
        private val TAB_TITLE = intArrayOf(
                R.string.followers,
                R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userID = intent.getIntExtra(EXTRA_USER_ID, 0)
        val user = intent.getStringExtra(EXTRA_USER)
        val avatar = intent.getStringExtra(EXTRA_USER_AVATAR)
        val githubLink = intent.getStringExtra(EXTRA_USER_LINK)
        val mBundle = Bundle()
        mBundle.putString(EXTRA_USER, user)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.setUserProfile(user.toString())
        viewModel.getUserProfile().observe(this, { userList ->
            if (userList != null) {
                binding.apply {
                    Glide.with(this@ProfileActivity)
                            .load(userList.avatar)
                            .apply(RequestOptions().override(100, 100))
                            .into(imgAvatar)
                    txtRepo.text = userList.repository?.let { decimalFormat(it) }
                    txtFollowing.text = userList.following?.let { decimalFormat(it) }
                    txtFollowers.text = userList.followers?.let { decimalFormat(it) }
                    txtName.text = userList.name?.let { checkNull(it) }
                    txtLocation.text = userList.location?.let { checkNull(it) }
                    txtCompany.text = userList.company?.let { checkNull(it) }
                    txtLink.text = userList.githubLink
                }
            }
        })

        binding.toolbarTitle.text = user
        setSupportActionBar(binding.toolbarProfile)

        binding.btnBack.setOnClickListener {
            finish()
        }

        if(!isConnected(this)) showErrorDialog()

        val sectionPagerAdapter = SectionPagerAdapter(this, mBundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            TabLayoutMediator(tabs, viewPager){ tab, position ->
                tab.text = resources.getString(TAB_TITLE[position])
            }.attach()
        }

        var statusFav = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.countFav(userID)
            withContext(Dispatchers.Main){
                if(count != null){
                    statusFav = if(count > 0){
                        binding.btnFavorite.setImageResource(R.drawable.ic_filled_favorite)
                        true
                    } else{
                        binding.btnFavorite.setImageResource(R.drawable.ic_border_favorite)
                        false
                    }
                }
            }
        }

        binding.btnFavorite.setOnClickListener{
            statusFav = !statusFav
            if(statusFav){
                viewModel.insertFav(userID, user.toString(), avatar.toString(), githubLink.toString())
                binding.btnFavorite.setImageResource(R.drawable.ic_filled_favorite)
                Toast.makeText(this, R.string.favorite_checked, Toast.LENGTH_SHORT).show()
            } else{
                viewModel.deleteFav(userID, user.toString(), avatar.toString(), githubLink.toString())
                binding.btnFavorite.setImageResource(R.drawable.ic_border_favorite)
                Toast.makeText(this, R.string.favorite_unchecked, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkNull(it: String): String {
        if(it == "null") return "-"
        return it
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

    @SuppressLint("NewApi")
    private fun isConnected(profileActivity: ProfileActivity): Boolean{
        val connectivityManager: ConnectivityManager = profileActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }

    private fun decimalFormat(number: Number): String{
        val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
        val numValue: Long = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        return if (value >= 4 && base < suffix.size) {
            DecimalFormat("#0.0").format(numValue / 10.0.pow((base * 3).toDouble())) + suffix[base]
        } else {
            DecimalFormat("#,##0").format(numValue)
        }
    }
}