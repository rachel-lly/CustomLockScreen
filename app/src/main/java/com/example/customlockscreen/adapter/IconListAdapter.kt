package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.IconListItemBinding
import java.util.HashMap


class IconListAdapter(val context: Context,clickListener: ClickListener) :
        RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    interface ClickListener{
        fun onClick(position: Int)
    }

    private lateinit var  binding : IconListItemBinding

    private val mClickListener = clickListener

    private var holderList = HashMap<Int,IconListAdapter.ViewHolder>()

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


        val holder = ViewHolder(binding)

        holder.icon.setOnClickListener {

            val position = holder.adapterPosition

            if(lastposition!=-1){
                holderList.get(lastposition)?.checkbox?.visibility = View.GONE
            }


            mClickListener.onClick(position)

            holder.checkbox.isChecked = true
            holder.checkbox.visibility = View.VISIBLE
            lastposition = position


            notifyDataSetChanged()

        }



        return holder
    }




    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.setImageResource(iconList[position])
        holderList.put(position,holder)
    }


    override fun getItemCount() = iconList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position


}