package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.R
import com.example.customlockscreen.activity.DetailActivity
import com.example.customlockscreen.activity.LABEL
import com.example.customlockscreen.activity.LABEL_IS_LOCK
import com.example.customlockscreen.databinding.CardItemLinearBinding
import com.example.customlockscreen.model.bean.Label
import kotlin.math.abs

class LabelLinearAdapter(val context: Context, var labelList:List<Label>,val isLock:Boolean) :
    RecyclerView.Adapter<LabelLinearAdapter.ViewHolder>() {

    private lateinit var  binding : CardItemLinearBinding


    inner class ViewHolder(binding:  CardItemLinearBinding) : RecyclerView.ViewHolder(binding.root) {
        val LabelText : TextView = binding.labelText
        val LabelDay : TextView = binding.labelDay
        val labelJustText:TextView = binding.dayJustText
        val contentLayout:LinearLayout = binding.contentLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  CardItemLinearBinding.inflate(LayoutInflater.from(context))

        val holder = ViewHolder(binding)

        holder.contentLayout.setOnClickListener {

            val position = holder.adapterPosition
            val label = labelList[position]

            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(LABEL,label)
                putExtra(LABEL_IS_LOCK,isLock)
            }
            context.startActivity(intent)

        }

        return holder
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = labelList[position]
        holder.LabelText.text = label.text
        holder.LabelDay.text = abs(label.day).toString()
        if(label.day>=0){
            holder.LabelDay.setBackgroundColor(context.resources.getColor(R.color.note_list_future_light,context.theme))
            holder.labelJustText.setBackgroundColor(context.resources.getColor(R.color.note_list_future_dark,context.theme))
        }else{
            holder.LabelDay.setBackgroundColor(context.resources.getColor(R.color.note_list_history_light,context.theme))
            holder.labelJustText.setBackgroundColor(context.resources.getColor(R.color.note_list_history_dark,context.theme))
        }


    }

    override fun getItemCount() = labelList.size



}