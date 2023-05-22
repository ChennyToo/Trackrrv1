package com.example.trackrrv1

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackrrv1.databinding.ListItemLayoutCalendarBinding

class CalendarViewHolder(val binding:ListItemLayoutCalendarBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var dayNode: Day

    fun bindDay(day : Day) {
        dayNode = day

        val dayMonth = dayNode.dayOfMonth
        val dayWeek = dayNode.dayOfWeek

        binding.dayMonthTV.text = dayMonth.toString()
        binding.dayWeekTV.text = dayWeek
    }

}