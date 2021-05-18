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
import com.example.customlockscreen.databinding.SortNoteListItemBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase
import java.util.*

class SortNoteListAdapter(val context: Context, val sortNoteList:List<SortNote>) :
        RecyclerView.Adapter<SortNoteListAdapter.ViewHolder>() {


    private lateinit var  binding : SortNoteListItemBinding

    private val labelDao = DataBase.dataBase.labelDao()

    inner class ViewHolder(binding: SortNoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sortNoteText : TextView = binding.sortTx
        val sortNoteIcon : ImageView = binding.sortIcon


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

        val sameSortNoteLabelList = labelDao.getSameSortNoteLabelList(sortNote.name)

        holder.sortNoteCount.text = sameSortNoteLabelList.size.toString()
        // TODO: 2021/5/18 添加完label再弄
        if(sameSortNoteLabelList.size!=0){
            val minLabel:Label = Collections.min(sameSortNoteLabelList)
            holder.latestNoteName.text = minLabel.text

            if(minLabel.day>0){
                holder.latestNoteDay.text = "还有 ${minLabel.day} 天"
            }else{
                holder.latestNoteDay.text = "已经 ${Math.abs(minLabel.day)} 天"
            }


        }else{
            holder.latestNoteName.visibility = View.GONE
            holder.latestNoteDay.visibility = View.GONE
        }


    }

    override fun getItemCount() = sortNoteList.size


}