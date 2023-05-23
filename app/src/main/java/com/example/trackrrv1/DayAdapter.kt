package com.example.trackrrv1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutCalendarBinding
import java.time.LocalDate
import java.time.LocalDateTime

class DayAdapter (var days: MutableList<Day>, var calendar : CalendarFragment, val monthOfYear : Int) : RecyclerView.Adapter<CalendarViewHolder>() {
    lateinit var binding: ListItemLayoutCalendarBinding
    lateinit var previousClickedView : View
    var positionOfSelected =-1

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
        if(day.dayOfMonth == LocalDateTime.now().dayOfMonth){//TODO add more conditionals so that
            //TODO upon creation, only today is selected and not other months
            positionOfSelected = position
            binding.dayNode.setBackgroundResource(R.drawable.cal_selected_day)
            previousClickedView = holder.itemView
        }
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.dayNode -> {
                        if(positionOfSelected != holder.bindingAdapterPosition) {//Condition makes it so that
                            //When pressing the day that is already selected, it will not do anything
                            positionOfSelected = holder.bindingAdapterPosition // updates the selected position if user selects new dat
                            view.findViewById<View>(R.id.dayNode)
                                .setBackgroundResource(R.drawable.cal_selected_day)  //makes the clicked day selected
                            previousClickedView.findViewById<View>(R.id.dayNode)
                                .setBackgroundResource(R.drawable.cal_unselected_day) //makes the previous clicked day unselected
                            previousClickedView = view //updates the previous clicked view to the view just pressed
                            var time : LocalDate = LocalDate.of(LocalDateTime.now().year, monthOfYear, positionOfSelected)//position of selected is the day selected based on index

                            calendar.displayDataForDate(time)//Input date of note selected
                        }

                    }
                }
            }
        binding.dayNode.setOnClickListener(buttonsClickListener)    }

}