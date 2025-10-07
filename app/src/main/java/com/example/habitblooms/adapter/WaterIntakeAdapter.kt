package com.example.habitblooms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habitblooms.R
import com.example.habitblooms.data.WaterIntake

class WaterIntakeAdapter(
    var intakes: List<WaterIntake>
) : RecyclerView.Adapter<WaterIntakeAdapter.WaterIntakeViewHolder>() {

    class WaterIntakeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.tv_time)
        val amountTextView: TextView = itemView.findViewById(R.id.tv_amount)
        val typeTextView: TextView = itemView.findViewById(R.id.tv_type)
        val cumulativeTextView: TextView = itemView.findViewById(R.id.tv_cumulative)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterIntakeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_water_intake, parent, false)
        return WaterIntakeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WaterIntakeViewHolder, position: Int) {
        val intake = intakes[position]
        holder.timeTextView.text = intake.time
        holder.amountTextView.text = "${intake.amountMl}ml"
        holder.typeTextView.text = intake.type
        holder.cumulativeTextView.text = "${String.format("%.1f", intake.cumulativeL)}L"
    }

    override fun getItemCount(): Int = intakes.size

    fun updateIntakes(newIntakes: List<WaterIntake>) {
        intakes = newIntakes
        notifyDataSetChanged()
    }
}