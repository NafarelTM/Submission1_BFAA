package com.dicoding.submissionfundamental1.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.submissionfundamental1.fragment.FollowersFragment
import com.dicoding.submissionfundamental1.fragment.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, mBundle: Bundle): FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle = mBundle

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle

        return fragment as Fragment
    }

}