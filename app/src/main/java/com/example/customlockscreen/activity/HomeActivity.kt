package com.example.customlockscreen.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.customlockscreen.Fragment.MineFragment
import com.example.customlockscreen.Fragment.NoteListFragment
import com.example.customlockscreen.Fragment.NoteSortFragment
import com.example.customlockscreen.Fragment.SettingFragment
import com.example.customlockscreen.R
import com.example.customlockscreen.adapter.HeaderSortNoteListAdapter
import com.example.customlockscreen.adapter.PagerAdapter
import com.example.customlockscreen.databinding.ActivityHomeBinding
import com.example.customlockscreen.model.db.DataBase

class HomeActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityHomeBinding
    private val itemIdArray :IntArray = intArrayOf(R.id.item_home, R.id.item_note, R.id.item_mine, R.id.item_setting)

    private var fragmentList = ArrayList<Fragment>()

    private val sortNoteDao = DataBase.dataBase.sortNoteDao()

    private val labelDao = DataBase.dataBase.labelDao()

    private lateinit var adapter:HeaderSortNoteListAdapter

    private lateinit var clickListener: HeaderSortNoteListAdapter.OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.homeToolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.mipmap.menu)
        }







        binding.navigationView.setNavigationItemSelectedListener {
            // TODO: 2021/4/21 侧拉栏点击事件
            binding.drawerLayout.closeDrawers()
//            when(it.itemId){
//                R.id.life ->{
//
//                }
//
//                R.id.work ->{
//
//                }
//            }

            true
        }


        val list = sortNoteDao.getAllSortNotes()

        clickListener =object :HeaderSortNoteListAdapter.OnClickListener{
            override fun onClick(sortNoteName: String) {
                // TODO: 2021/5/22 获取点击的分类本并显示

            }

        }

        adapter = HeaderSortNoteListAdapter(this,list,clickListener)

        val headerLayout = binding.navigationView.inflateHeaderView(R.layout.header_layout)
        val recycleView = headerLayout.findViewById<RecyclerView>(R.id.drawlayout_headerlayout_recycleview)

        val textView = headerLayout.findViewById<TextView>(R.id.all_labels_count)
        headerLayout.findViewById<RelativeLayout>(R.id.all_labels_layout).setOnClickListener {
            // TODO: 2021/5/22 选择全部事件显示
        }

        recycleView.adapter = adapter
        recycleView.layoutManager = GridLayoutManager(this, 1)


        binding.homeToolbar.setNavigationOnClickListener {

            val list = sortNoteDao.getAllSortNotes()
            adapter.sortNoteList = list
            adapter.notifyDataSetChanged()

            textView.text = list.size.toString()

            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }



        fragmentList.add(NoteListFragment())
        fragmentList.add(NoteSortFragment())
        fragmentList.add(MineFragment())
        fragmentList.add(SettingFragment())



        binding.homeViewPager.adapter = PagerAdapter(this,fragmentList)
        binding.homeViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.homeNavigationView.selectedItemId = itemIdArray[position]
                when(position){
                    0->{
                        binding.homeToolbar.title = "主页"
                    }

                    1->{
                        binding.homeToolbar.title = "分类管理"
                    }

                    2->{
                        binding.homeToolbar.title = "我的"
                    }

                    3->{
                        binding.homeToolbar.title = "设置"
                    }
                }
            }
        })


        binding.homeNavigationView.inflateMenu(R.menu.home_navigation_view)
        binding.homeNavigationView.setOnNavigationItemSelectedListener {

            for(i in 0..fragmentList.size-1){
                if(it.itemId == itemIdArray[i]){
                    binding.homeViewPager.currentItem = i
                }
            }


            return@setOnNavigationItemSelectedListener true
        }




        setContentView(binding.root)
    }




}