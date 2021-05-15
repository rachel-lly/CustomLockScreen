package com.example.customlockscreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.IconListItemBinding


class IconListAdapter(val context: Context,clickListener: ClickListener) :
        RecyclerView.Adapter<IconListAdapter.ViewHolder>() {


    interface ClickListener{
        fun onClick(iconName:String)
    }


    private lateinit var  binding : IconListItemBinding

    private var mClickListener = clickListener

    private val iconList = intArrayOf(R.mipmap.cat,R.mipmap.owl, R.mipmap.flamingo,R.mipmap.cactus, R.mipmap.marigold,R.mipmap.umbrella,
            R.mipmap.happy, R.mipmap.rocket,R.mipmap.yellow_star, R.mipmap.love_heart, R.mipmap.earth,R.mipmap.music,
            R.mipmap.computer, R.mipmap.cake, R.mipmap.diamond, R.mipmap.work_color, R.mipmap.life_color, R.mipmap.anniverity_color )


    inner class ViewHolder(binding: IconListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.icon
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  IconListItemBinding.inflate(LayoutInflater.from(context))

        val holder = ViewHolder(binding)
        holder.icon.setOnClickListener {
            // TODO: 2021/5/15 设置点击icon效果

            var iconName:String = context.resources.getResourceEntryName(it.id)
            mClickListener.onClick(iconName)

        }

        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.setImageResource(iconList[position])
    }


    override fun getItemCount() = iconList.size



}