package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.databinding.IconListItemBinding
import com.example.customlockscreen.util.Code
import java.util.HashMap

class IconListAdapter(val context: Context,clickListener: ClickListener,defaultPosition: Int) :
        RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    interface ClickListener{
        fun onClick(position: Int)
    }

    private lateinit var  binding : IconListItemBinding

    private val mClickListener = clickListener

    private var holderList = HashMap<Int,IconListAdapter.ViewHolder>()

    private var lastposition = defaultPosition



    inner class ViewHolder(binding: IconListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.icon
        val checkbox = binding.checkbox
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  IconListItemBinding.inflate(LayoutInflater.from(context))


        val holder = ViewHolder(binding)

        holder.icon.setOnClickListener {

            val position = holder.adapterPosition


            holderList[lastposition]?.checkbox?.visibility = View.GONE


            mClickListener.onClick(position)

            holder.checkbox.isChecked = true
            holder.checkbox.visibility = View.VISIBLE
            lastposition = position

            notifyDataSetChanged()

        }

        return holder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position==lastposition){
            holder.checkbox.isChecked = true
            holder.checkbox.visibility = View.VISIBLE
        }
        holder.icon.setImageResource(Code.iconList[position])
        holderList[position] = holder
    }


    override fun getItemCount() = Code.iconList.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position


}