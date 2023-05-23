package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trackrrv1.databinding.FragmentCalendarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime


class CalendarFragment : Fragment() {
    var _binding: FragmentCalendarBinding? = null
    val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()
    private val calViewModel: CalendarViewModel by activityViewModels()
    lateinit var dbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        var densityOfScreen = getResources().getDisplayMetrics().density
        density = densityOfScreen
        dbRef = Firebase.database.reference

        Thread {//lessen system load
            activity?.runOnUiThread{
                val mAdapter = DayAdapter(calViewModel.createDays(LocalDateTime.now().monthValue), this, 5)
                binding.recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0) //prevents bug where some items may disappear by setting the view to be invisible
                binding.recyclerView.adapter = mAdapter
                binding.recyclerView.layoutManager?.scrollToPosition(LocalDateTime.now().dayOfMonth - 1)//capability to start the screen with correct day in position
                //position parameter is currentDay - 1 because of the invisibleDay
//                var currentView : View = binding.recyclerView.layoutManager.findViewByPosition(LocalDateTime.now().dayOfMonth)!!
            }
        }.start()





        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun displayDataForDate(time : LocalDate){
        var dayCalorie = 0
        var dayFat = 0
        var dayProtein = 0
        var dayCarb = 0
        dbRef.get().addOnSuccessListener { snapshot ->
            var foodSnapShot = snapshot.child(viewModel.year).child(time.month.toString()).child(
                time.dayOfMonth.toString()
            ).children
            for (foodItem in foodSnapShot) {
                dayCalorie += foodItem.child("calories").value.toString().toInt()
                dayFat = foodItem.child("fat").value.toString().toInt()
                dayProtein += foodItem.child("protein").value.toString().toInt()
                dayCarb += foodItem.child("carbohydrate").value.toString().toInt()
            }


            binding.thisCalorieTV.text = dayCalorie.toString()//TODO comma parsing
            binding.proteinAmountTV.text = dayProtein.toString() + "g"
            binding.carbAmountTV.text = dayCarb.toString() + "g"
            binding.fatAmountTV.text = dayFat.toString() + "g"

            binding.proteinProgress.progress = ((dayProtein * 100 / Constants.proteinIntake))
            binding.carbProgress.progress = ((dayCarb * 100 / Constants.carbIntake))
            binding.fatProgress.progress = ((dayFat * 100 / Constants.fatIntake))



        }
    }//TODO TEST IF WORKS LATER



    companion object{
        var density : Float = 0f
    }


}