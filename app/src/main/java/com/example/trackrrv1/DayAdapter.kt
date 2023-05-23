package com.example.trackrrv1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutCalendarBinding

class DayAdapter (var days: MutableList<Day>) : RecyclerView.Adapter<CalendarViewHolder>() {
    lateinit var binding: ListItemLayoutCalendarBinding
    lateinit var previousClickedView : View

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
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.dayNode -> {
                        view.findViewById<View>(R.id.dayNode).setBackgroundResource(R.drawable.cal_selected_day)
                        previousClickedView
                    }
                }
            }
        binding.dayNode.setOnClickListener(buttonsClickListener)    }

}