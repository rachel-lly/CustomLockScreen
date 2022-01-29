package com.example.customlockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.databinding.HeaderLayoutSortNoteListItemBinding
import com.example.customlockscreen.model.bean.SortNote
import com.example.customlockscreen.model.db.DataBase

class HeaderSortNoteListAdapter(val context: Context, var sortNoteList:List<SortNote>,onClickListener: OnClickListener) :
        RecyclerView.Adapter<HeaderSortNoteListAdapter.ViewHolder>() {

    interface OnClickListener{
        fun onClick(sortNoteName:String)
    }


    private val labelDao = DataBase.dataBase.labelDao()

    private val mClickListener = onClickListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : HeaderLayoutSortNoteListItemBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.header_layout_sort_note_list_item,parent,false)


        val holder = ViewHolder(binding.root)
        holder.itemView.setOnClickListener {

            val position = holder.absoluteAdapterPosition
            val sortNote = sortNoteList[position]

            mClickListener.onClick(sortNote.name)
            
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bind = DataBindingUtil.bind<HeaderLayoutSortNoteListItemBinding>(holder.itemView)

        bind?.sortnote = sortNoteList[position]

        val iconId:Int =
            context.resources.getIdentifier(bind?.sortnote?.iconName, "mipmap", context.packageName)
        bind?.sortIcon?.setImageResource(iconId)

        bind?.sortCount?.text = labelDao.getLabelCountBySameSort(bind?.sortnote!!.name).toString()

        bind?.executePendingBindings()

    }

    override fun getItemCount() = sortNoteList.size


}