package com.example.trackrrv1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutCalendarBinding

class DayAdapter (var days: MutableList<Day>) : RecyclerView.Adapter<CalendarViewHolder>() {
    lateinit var binding: ListItemLayoutCalendarBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemLayoutCalendarBinding.inflate(layoutInflater, parent, false)
        return CalendarViewHolder(binding)   }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        holder.bindDay(day)
    }

}