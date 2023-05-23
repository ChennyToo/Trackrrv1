package com.example.trackrrv1

import android.util.Log
import android.view.View
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

        if(dayWeek.equals("00") || dayWeek.equals("99")){ //this adds space to beginning and end of RecyclerView
            //achieved by setting all components invisible
            binding.dayMonthTV.setVisibility(View.INVISIBLE)
            binding.dayWeekTV.setVisibility(View.INVISIBLE)
            binding.dayNode.setVisibility(View.INVISIBLE)
            //sets width of the empty view whilst scaling based on screen density
            var dp : Float = 70f / CalendarFragment.density
            binding.dayNode.layoutParams.width = dp.toInt()
        }
    }

}