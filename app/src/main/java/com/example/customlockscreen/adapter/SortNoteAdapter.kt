package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.activity.AddNoteActivity
import com.example.customlockscreen.activity.SORT_NOTE_TEXT
import com.example.customlockscreen.databinding.SortNoteCardItemBinding
import com.example.customlockscreen.model.SortNote

class SortNoteAdapter (val context: Context, val sortNoteList:List<SortNote>) :
        RecyclerView.Adapter<SortNoteAdapter.ViewHolder>() {

    private lateinit var  binding : SortNoteCardItemBinding

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

            val intent = Intent(context, AddNoteActivity::class.java).apply {
                putExtra(SORT_NOTE_TEXT,sortNote.name)
            }
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