package com.example.customlockscreen.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.activity.DetailActivity
import com.example.customlockscreen.activity.LABEL_DATE
import com.example.customlockscreen.activity.LABEL_DAY
import com.example.customlockscreen.activity.LABEL_TEXT
import com.example.customlockscreen.databinding.CardItemGridBinding
import com.example.customlockscreen.model.Label


class LabelGridAdapter(val context: Context, val labelList:List<Label>) :
        RecyclerView.Adapter<LabelGridAdapter.ViewHolder>() {

    private lateinit var  binding : CardItemGridBinding

    inner class ViewHolder(binding: CardItemGridBinding) : RecyclerView.ViewHolder(binding.root) {
        val LabelText : TextView = binding.labelText
        val LabelDay : TextView = binding.labelDay
        val LabelDate : TextView = binding.labelDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =  CardItemGridBinding.inflate(LayoutInflater.from(context))

        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val label = labelList[position]

            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(LABEL_TEXT,label.text)
                putExtra(LABEL_DAY, label.day.toString())
                putExtra(LABEL_DATE, label.date)
            }
            context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = labelList[position]
        holder.LabelText.text = label.text
        holder.LabelDay.text = label.day.toString()
        holder.LabelDate.text = label.date
    }


    override fun getItemCount() = labelList.size



}