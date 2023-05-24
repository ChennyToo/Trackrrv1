package com.example.trackrrv1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutCalendarBinding
import java.time.LocalDate
import java.time.LocalDateTime

class DayAdapter (var days: MutableList<Day>, var calendar : CalendarFragment, val monthOfYear : Int) : RecyclerView.Adapter<CalendarViewHolder>() {
    lateinit var binding: ListItemLayoutCalendarBinding
    var previousClickedView : View? = null//Used to remove the selected background image of the previous node when selecting new node
    var positionOfSelected =-1//if the position of selected is same node, nothing will happen

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
        if(day.dayOfMonth == LocalDate.now().dayOfMonth && startScreen){
            //upon creation, only today is selected and not other nodes when changing to past months
            startScreen = false
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

                            if (previousClickedView != null){//if there is no selected view, dont run the conditional
                            previousClickedView!!.findViewById<View>(R.id.dayNode)
                                .setBackgroundResource(R.drawable.cal_unselected_day)} //makes the previous clicked day unselected

                            previousClickedView = view //updates the previous clicked view to the view just pressed
                            var time : LocalDate = LocalDate.of(LocalDateTime.now().year, monthOfYear, positionOfSelected)//position of selected is the day selected based on index

                            calendar.displayDataForDate(time)//Input date of note selected
                        }

                    }
                }
            }
        binding.dayNode.setOnClickListener(buttonsClickListener)    }

    companion object{
        var startScreen = true//boolean to determine whether or not there should be a selected node by default

    }

}