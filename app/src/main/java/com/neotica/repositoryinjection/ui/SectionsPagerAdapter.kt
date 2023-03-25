package com.neotica.repositoryinjection.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//Step 7: Create PagerAdapter
//Step 7.1 Add internal constructor with AppCompatActivity as its parameter
//Step 7.2 Returns to FragmentStateAdapter with the context of AppCompatActivity
class SectionsPagerAdapter internal constructor(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        //Step 7.3 Define fragment that will be put inside the adapter
        val fragment = NewsFragment()
        val bundle = Bundle()
        //Step 7.4 Create condition for tabs position
        if (position == 0) {
            bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_NEWS)
        } else {
            bundle.putString(NewsFragment.ARG_TAB, NewsFragment.TAB_BOOKMARK)
        }
        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}