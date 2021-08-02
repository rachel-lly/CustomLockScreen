package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.databinding.HeaserLayoutSortNoteListItemBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase

class HeaderSortNoteListAdapter(val context: Context, var sortNoteList:List<SortNote>,onClickListener: OnClickListener) :
        RecyclerView.Adapter<HeaderSortNoteListAdapter.ViewHolder>() {

    interface OnClickListener{
        fun onClick(sortNoteName:String)
    }


    private lateinit var  binding : HeaserLayoutSortNoteListItemBinding

    private val labelDao = DataBase.dataBase.labelDao()

    private val mClickListener = onClickListener

    inner class ViewHolder(binding: HeaserLayoutSortNoteListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val sortNoteText : TextView = binding.sortTx
        val sortNoteIcon : ImageView = binding.sortIcon
        val sortNoteCount : TextView = binding.sortCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  HeaserLayoutSortNoteListItemBinding.inflate(LayoutInflater.from(context))


        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {

            val position = holder.adapterPosition
            val sortNote = sortNoteList[position]

            mClickListener.onClick(sortNote.name)
            
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sortNote = sortNoteList[position]
        holder.sortNoteText.text = sortNote.name
        val iconId:Int = context.resources.getIdentifier(sortNote.iconName,"mipmap",context.packageName)
        holder.sortNoteIcon.setImageResource(iconId)

        val sameSortNoteLabelList = labelDao.getSameSortNoteLabelList(sortNote.name)

        holder.sortNoteCount.text = sameSortNoteLabelList.size.toString()
    }

    override fun getItemCount() = sortNoteList.size


}