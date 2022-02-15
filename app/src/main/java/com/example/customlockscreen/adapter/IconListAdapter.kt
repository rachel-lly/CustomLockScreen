package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.HeaderLayoutSortNoteListItemBinding
import com.example.customlockscreen.databinding.IconListItemBinding
import com.example.customlockscreen.util.Code
import java.util.HashMap

class IconListAdapter(val context: Context,clickListener: ClickListener,defaultPosition: Int) :
        RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    interface ClickListener{
        fun onClick(position: Int)
    }

    private val mClickListener = clickListener

    private var holderList = HashMap<Int,IconListAdapter.ViewHolder>()

    private var lastposition = defaultPosition


    inner class ViewHolder(binding: IconListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.icon
        val checkbox = binding.checkbox
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : IconListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.icon_list_item,parent,false)


        val holder = ViewHolder(binding)

        binding.icon.setOnClickListener {

            val position = holder.absoluteAdapterPosition


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