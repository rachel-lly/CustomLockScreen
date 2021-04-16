package com.example.customlockscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.customlockscreen.Fragment.AddNoteFragment

import com.example.customlockscreen.Fragment.NoteListFragment
import com.example.customlockscreen.databinding.ActivityHomeBinding

private const val LINEAR_LIST = "LINEAR_LIST"
private const val GRID_LIST = "GRID_LIST"


class HomeActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityHomeBinding
    private val itemIdArray :IntArray = intArrayOf(R.id.item_home,R.id.item_note,R.id.item_mine,R.id.item_more)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.homeToolbar)

        binding.homeToolbar.setNavigationIcon(R.mipmap.menu)

        binding.homeNavigationView.inflateMenu(R.menu.home_navigation_view)
        binding.homeNavigationView.setOnNavigationItemSelectedListener {
            // TODO: 2021/4/15 目前是2 记得改
            for(i in 0..2){
                if(it.itemId == itemIdArray[i]){
                    binding.homeViewPager.currentItem = i
                }
            }


            return@setOnNavigationItemSelectedListener true
        }

        var fragmentList = ArrayList<Fragment>()
        fragmentList.add(NoteListFragment())
        fragmentList.add(AddNoteFragment())
        binding.homeViewPager.adapter = PagerAdapter(this,fragmentList)
        binding.homeViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.homeNavigationView.selectedItemId = itemIdArray[position]
            }
        })


        setContentView(binding.root)
    }





}