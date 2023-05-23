package com.example.trackrrv1

import android.util.Log
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

class CalendarViewModel: ViewModel() {
    var year = LocalDateTime.now().year
    lateinit var listOfDays : MutableList<Day>


    fun createDays(month : Int) : MutableList<Day>{
        listOfDays = mutableListOf(Day("00", 0))//reset list everytime you want a new month
        //start with 0 day to add space on recycler view
        var maxDaysMonth = mutableListOf(31,29,31,30,31,30,31,31,30,31,30,31)
        if(year%4==0&&year%100!=0||year%100==0){//Leap Year Consideration
            maxDaysMonth[1] = 29
        }

        else {
            maxDaysMonth[1] = 28
        }
        for (day in 1..maxDaysMonth[month - 1]){//goes through entire month finding the dayOfWeek of each day
            var time = LocalDate.of(year, month, day)
            var dayOfWeek = time.dayOfWeek.toString()
            var firstLetter = dayOfWeek.substring(0, 1)
            var secondLetter = dayOfWeek.substring(1, 2).lowercase(Locale.ROOT)
            dayOfWeek = firstLetter + secondLetter //Gets the day of week in two characters, first letter in caps
            listOfDays.add(Day(dayOfWeek, day))
        }

        listOfDays.add(Day("99", -1))
        Log.d("MainActivity", "$listOfDays")
            return listOfDays
    }
}