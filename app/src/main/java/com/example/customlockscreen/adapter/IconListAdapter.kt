package com.example.customlockscreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.IconListItemBinding
import java.util.HashMap


class IconListAdapter(val context: Context) :
        RecyclerView.Adapter<IconListAdapter.ViewHolder>() {



    private lateinit var  binding : IconListItemBinding



    var positionMap = HashMap<Int,Boolean>()

    private var lastposition = -1

     val iconList = intArrayOf(R.mipmap.cat,R.mipmap.owl, R.mipmap.flamingo,R.mipmap.cactus, R.mipmap.marigold,R.mipmap.umbrella,
            R.mipmap.happy, R.mipmap.rocket,R.mipmap.yellow_star, R.mipmap.love_heart, R.mipmap.earth,R.mipmap.music,
            R.mipmap.computer, R.mipmap.cake, R.mipmap.diamond, R.mipmap.work_color, R.mipmap.life_color, R.mipmap.anniverity_color )


    inner class ViewHolder(binding: IconListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.icon
        val checkbox = binding.checkbox
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  IconListItemBinding.inflate(LayoutInflater.from(context))

        for(position in 0..iconList.size-1){
            positionMap.put(position,false)
        }

        val holder = ViewHolder(binding)


        holder.icon.setOnClickListener {
            val position = holder.adapterPosition

            when(lastposition){
                position->{
                    holder.checkbox.visibility = View.GONE
                    lastposition = -1
                    positionMap.put(position,false)
                }
                else ->{
                    holder.checkbox.isChecked = true
                    holder.checkbox.visibility = View.VISIBLE
                    lastposition = position
                    positionMap.put(position,true)
                }
            }

            notifyDataSetChanged()
        }



        return holder
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.setImageResource(iconList[position])
    }


    override fun getItemCount() = iconList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

}