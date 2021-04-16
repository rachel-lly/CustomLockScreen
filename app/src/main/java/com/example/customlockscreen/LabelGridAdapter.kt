package com.example.customlockscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customlockscreen.databinding.CardItemGridBinding


class LabelGridAdapter(val context: Context, val labelList:List<Label>) :
        RecyclerView.Adapter<LabelGridAdapter.ViewHolder>() {

    private lateinit var  binding : CardItemGridBinding

    inner class ViewHolder(binding: CardItemGridBinding) : RecyclerView.ViewHolder(binding.root) {
        val LabelText : TextView = binding.labelText
        val LabelDay : TextView = binding.labelDay
        val LabelDate : TextView = binding.labelDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelGridAdapter.ViewHolder {
        binding =  CardItemGridBinding.inflate(LayoutInflater.from(context))
//        val view = LayoutInflater.from(context).inflate(R.layout.card_item,parent,false)

        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val Label = labelList[position]
            // TODO: 2021/4/14 点击便签跳转详细页面
//            val intent = Intent(context,HomeActivity::class.java).apply {
//                putExtra(LabelActivity.Label_NAME,Label.LabelName)
//                putExtra(LabelActivity.Label_IMAGE_ID,Label.LabelImageId)
//            }
//            context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: LabelGridAdapter.ViewHolder, position: Int) {
        val label = labelList[position]
        holder.LabelText.text = label.text
        holder.LabelDay.text = label.day.toString()
        holder.LabelDate.text = label.date
    }


    override fun getItemCount() = labelList.size



}