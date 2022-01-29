package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.HeaderLayoutSortNoteListItemBinding
import com.example.customlockscreen.databinding.SortNoteCardItemBinding
import com.example.customlockscreen.model.bean.SortNote

class SortNoteAdapter (val context: Context, var sortNoteList:List<SortNote>, clickListener:ClickListener) :
        RecyclerView.Adapter<SortNoteAdapter.ViewHolder>() {

    interface ClickListener{
        fun onClick(SortNoteName:String)
    }

    private var mClickListener: ClickListener = clickListener


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : SortNoteCardItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.sort_note_card_item,parent,false)


        val holder = ViewHolder(binding.root)
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val sortNote = sortNoteList[position]
            sortNote.name.let { mClickListener.onClick(it) }
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bind = DataBindingUtil.bind<SortNoteCardItemBinding>(holder.itemView)

        val sortNote = sortNoteList[position]
        bind!!.sortTx.text = sortNote.name
        val iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)
        bind.sortIcon.setImageResource(iconId)

        bind.executePendingBindings()
    }

    override fun getItemCount() = sortNoteList.size


}