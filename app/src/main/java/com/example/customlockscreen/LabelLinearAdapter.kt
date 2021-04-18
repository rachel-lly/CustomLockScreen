package com.example.customlockscreen

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.databinding.CardItemLinearBinding

class LabelLinearAdapter(val context: Context, val labelList:List<Label>) :
    RecyclerView.Adapter<LabelLinearAdapter.ViewHolder>() {

    private lateinit var  binding : CardItemLinearBinding

    inner class ViewHolder(binding:  CardItemLinearBinding) : RecyclerView.ViewHolder(binding.root) {
        val LabelText : TextView = binding.labelText
        val LabelDay : TextView = binding.labelDay
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelLinearAdapter.ViewHolder {
        binding =  CardItemLinearBinding.inflate(LayoutInflater.from(context))

        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val label = labelList[position]

            val intent = Intent(context,DetailActivity::class.java).apply {
                putExtra(LABEL_TEXT,label.text)
                putExtra(LABEL_DAY, label.day.toString())
                putExtra(LABEL_DATE, label.date)
            }
            context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: LabelLinearAdapter.ViewHolder, position: Int) {
        val label = labelList[position]
        holder.LabelText.text = label.text
        holder.LabelDay.text = label.day.toString()
    }

    override fun getItemCount() = labelList.size


}