package com.example.customlockscreen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(activity: FragmentActivity,fragments: List<Fragment>) : FragmentStateAdapter(activity) {

    private  var fragmentList : List<Fragment> = fragments

    override fun getItemCount(): Int = this.fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

}