package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.HeaderLayoutSortNoteListItemBinding
import com.example.customlockscreen.view.activity.EditSortNoteActivity
import com.example.customlockscreen.databinding.SortNoteListItemBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.util.Code
import java.util.*
import kotlin.math.abs

class SortNoteListAdapter(val context: Context, var sortNoteList:List<SortNote>,var labelList: List<Label>,deleteOnClickListener: deleteOnClickListener) :
        RecyclerView.Adapter<SortNoteListAdapter.ViewHolder>() {

    private val listener = deleteOnClickListener

    interface deleteOnClickListener{
        fun delete(sortNote: SortNote)
    }

    inner class ViewHolder(binding: SortNoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sortNoteText : TextView = binding.sortTx
        val sortNoteIcon : ImageView = binding.sortIcon


        val sortNoteCount : TextView = binding.sortCount
        val latestNoteName :TextView = binding.sortNoteName
        val latestNoteDay :TextView = binding.sortNoteDay

        val editSortNote: TextView = binding.editSortNoteItem
        val deleteSortNote: TextView = binding.deleteSortNoteItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : SortNoteListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.sort_note_list_item,parent,false)


        val holder = ViewHolder(binding)
        holder.editSortNote.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val sortNote = sortNoteList[position]
            jumpToEditActivity(sortNote)
        }

        holder.deleteSortNote.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val sortNote = sortNoteList[position]
            listener.delete(sortNote)
        }

        return holder
    }

    private fun jumpToEditActivity(sortNote: SortNote){
        val intent = Intent(context,EditSortNoteActivity::class.java)
        intent.putExtra(Code.SORT_NOTE,sortNote)
        context.startActivity(intent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val sortNote = sortNoteList[position]
        val sortNoteName = sortNote.name
        holder.sortNoteText.text = sortNoteName
        val iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)
        holder.sortNoteIcon.setImageResource(iconId)

        val sameSortNoteLabelList = ArrayList<Label>()

        for(label in labelList){
            if(label.sortNote == sortNoteName){
                sameSortNoteLabelList.add(label)
            }
        }

        holder.sortNoteCount.text = sameSortNoteLabelList.size.toString()

        if(sameSortNoteLabelList.size!=0){

            holder.latestNoteName.visibility = View.VISIBLE
            holder.latestNoteDay.visibility = View.VISIBLE

            val minLabel:Label = Collections.min(sameSortNoteLabelList)
            holder.latestNoteName.text = minLabel.text

            if(minLabel.day>0){
                holder.latestNoteDay.text = "还有 ${minLabel.day} 天"
            }else{
                holder.latestNoteDay.text = "已经 ${abs(minLabel.day)} 天"
            }


        }else{
            holder.latestNoteName.visibility = View.INVISIBLE
            holder.latestNoteDay.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount() = sortNoteList.size

}