package com.example.customlockscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customlockscreen.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setSupportActionBar(binding.homeToolbar)
        binding.homeToolbar.setNavigationIcon(R.mipmap.menu)
        binding.homeNavigationView.inflateMenu(R.menu.home_navigation_view)
        binding.homeNavigationView.setOnNavigationItemReselectedListener { 
            when(it.itemId){
                // TODO: 2021/4/14 跳转 
            }
        }

        var labelList = ArrayList<Label>()
        labelList.add(Label("星期六",8))
        labelList.add(Label("星期五",9))
        labelList.add(Label("距离考试",18))
        labelList.add(Label("哈哈哈哈哈哈",118))
        labelList.add(Label("星期六",8000))
        labelList.add(Label("星期六",80))

        binding.homeRecyclerview.adapter = LabelAdapter(this,labelList)
        binding.homeRecyclerview.layoutManager = GridLayoutManager(this,1)


        setContentView(binding.root)
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.home_toolbar,menu)
        return true

    }
}