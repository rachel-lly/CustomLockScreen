package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.databinding.SortNoteCardItemBinding
import com.example.customlockscreen.model.SortNote

class SortNoteAdapter (val context: Context, val sortNoteList:List<SortNote>,clickListener:ClickListener) :
        RecyclerView.Adapter<SortNoteAdapter.ViewHolder>() {

    interface ClickListener{
        fun onClick(SortNoteName:String)
    }

    private lateinit var  binding : SortNoteCardItemBinding

    private var mClickListener: ClickListener = clickListener


    inner class ViewHolder(binding: SortNoteCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sortNoteText : TextView = binding.sortTx
        val sortNoteIcon : ImageView = binding.sortIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  SortNoteCardItemBinding.inflate(LayoutInflater.from(context))





        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val sortNote = sortNoteList[position]
            sortNote.name?.let { it -> mClickListener.onClick(it) }

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