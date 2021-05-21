package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.activity.EditSortNoteActivity
import com.example.customlockscreen.activity.SORT_NOTE
import com.example.customlockscreen.databinding.HeaserLayoutSortNoteListItemBinding
import com.example.customlockscreen.databinding.SortNoteListItemBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import java.util.*

class HeaderSortNoteListAdapter(val context: Context, var sortNoteList:List<SortNote>) :
        RecyclerView.Adapter<HeaderSortNoteListAdapter.ViewHolder>() {


    private lateinit var  binding : HeaserLayoutSortNoteListItemBinding

    private val labelDao = DataBase.dataBase.labelDao()

    inner class ViewHolder(binding: HeaserLayoutSortNoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sortNoteText : TextView = binding.sortTx
        val sortNoteIcon : ImageView = binding.sortIcon


        val sortNoteCount : TextView = binding.sortCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  HeaserLayoutSortNoteListItemBinding.inflate(LayoutInflater.from(context))


        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            // TODO: 2021/5/21 侧拉栏点击 
            val position = holder.adapterPosition
            val sortNote = sortNoteList[position]
            
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sortNote = sortNoteList[position]
        holder.sortNoteText.text = sortNote.name
        var iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)
        holder.sortNoteIcon.setImageResource(iconId)

        val sameSortNoteLabelList = labelDao.getSameSortNoteLabelList(sortNote.name)

        holder.sortNoteCount.text = sameSortNoteLabelList.size.toString()
    }

    override fun getItemCount() = sortNoteList.size


}