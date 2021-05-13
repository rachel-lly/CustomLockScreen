package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.activity.EditSortNoteActivity
import com.example.customlockscreen.activity.SORT_NOTE
import com.example.customlockscreen.databinding.SortNoteListItemBinding
import com.example.customlockscreen.model.SortNote

class SortNoteListAdapter(val context: Context, val sortNoteList:List<SortNote>, val clickListener:ClickListener) :
        RecyclerView.Adapter<SortNoteListAdapter.ViewHolder>() {

    interface ClickListener{
        fun onClick(SortNoteName:String)
    }

    private lateinit var  binding : SortNoteListItemBinding




    inner class ViewHolder(binding: SortNoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sortNoteText : TextView = binding.sortTx
        val sortNoteIcon : ImageView = binding.sortIcon

        // TODO: 2021/4/30 分类本 事件个数、最近事件名字和天数 
        val sortNoteCount : TextView = binding.sortCount
        val latestNoteName :TextView = binding.sortNoteName
        val latestNoteDay :TextView = binding.sortNoteDay
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  SortNoteListItemBinding.inflate(LayoutInflater.from(context))





        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val sortNote = sortNoteList[position]

            val intent = Intent(context,EditSortNoteActivity::class.java)
            intent.putExtra(SORT_NOTE,sortNote)
            context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sortNote = sortNoteList[position]
        holder.sortNoteText.text = sortNote.name
        var iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)
        holder.sortNoteIcon.setImageResource(iconId)
    }

    override fun getItemCount() = sortNoteList.size


}