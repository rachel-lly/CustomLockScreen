package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.view.activity.DetailActivity
import com.example.customlockscreen.databinding.CardItemLinearBinding
import com.example.customlockscreen.databinding.HeaderLayoutSortNoteListItemBinding
import com.example.customlockscreen.model.bean.Label
import com.example.customlockscreen.util.Code
import kotlin.math.abs

class LabelLinearAdapter(val context: Context, var labelList:List<Label>,val isLock:Boolean) :
    RecyclerView.Adapter<LabelLinearAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : CardItemLinearBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.card_item_linear,parent,false)

        val holder = ViewHolder(binding.root)

        holder.itemView.setOnClickListener {

            val position = holder.absoluteAdapterPosition
            val label = labelList[position]

            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(Code.LABEL,label)
                putExtra(Code.LABEL_IS_LOCK,isLock)
            }
            context.startActivity(intent)

        }

        return holder
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bind = DataBindingUtil.bind<CardItemLinearBinding>(holder.itemView)

        bind!!.label = labelList[position]

        if(bind.label!!.day>=0){
            bind.labelDay.setBackgroundColor(context.resources.getColor(R.color.note_list_future_light,context.theme))
            bind.dayJustText.setBackgroundColor(context.resources.getColor(R.color.note_list_future_dark,context.theme))
        }else{
            bind.labelDay.setBackgroundColor(context.resources.getColor(R.color.note_list_history_light,context.theme))
            bind.dayJustText.setBackgroundColor(context.resources.getColor(R.color.note_list_history_dark,context.theme))
        }

        bind.executePendingBindings()
    }

    override fun getItemCount() = labelList.size



}