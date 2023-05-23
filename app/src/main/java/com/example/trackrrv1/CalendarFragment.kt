package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.trackrrv1.databinding.FragmentCalendarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment() {
    var _binding: FragmentCalendarBinding? = null
    val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()
    private val calViewModel: CalendarViewModel by activityViewModels()
    lateinit var dbRef: DatabaseReference
    val formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu")
    val now = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        var densityOfScreen = getResources().getDisplayMetrics().density
        density = densityOfScreen
        dbRef = Firebase.database.reference
        displayDataForDate(now)
        displayDaysOfMonth(now.monthValue)
        binding.selectedMonthTV.text = Constants.monthList.get(now.monthValue - 1)
        binding.thisMonthTV.text = Constants.monthList.get(now.monthValue - 1)
        var initializationCheck = 0


        var monthAdapter = MonthAdapter(this, Constants.monthList)
        binding.monthSpinner.adapter = monthAdapter
        binding.monthSpinner.setPopupBackgroundResource(R.drawable.cal_spinnerbg) //sets the background
        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(initializationCheck !=0) {
                    displayDaysOfMonth(position + 1)//index starts at 0, month value starts at 1
                    binding.thisMonthTV.text = Constants.monthList.get(position)
                    binding.selectedMonthTV.text = Constants.monthList.get(position)
                }
                initializationCheck++
            }

        }
//        val buttonsClickListener: View.OnClickListener =
//            View.OnClickListener { view ->
//                when(view.id){
//                    R.id.carbIcon->{
//                        val position = binding.monthSpinner.selectedItemPosition
//                        Log.d("MainActivity", "$position")
//                    }
//                }
//            }
//
//        binding.carbIcon.setOnClickListener(buttonsClickListener)








        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun displayDaysOfMonth(month : Int){
        Thread {//lessen system load
            activity?.runOnUiThread{
                val mAdapter = DayAdapter(calViewModel.createDays(month), this, month)
                binding.recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0) //prevents bug where some items may disappear by setting the view to be invisible
                binding.recyclerView.adapter = mAdapter
                binding.recyclerView.layoutManager?.scrollToPosition(LocalDateTime.now().dayOfMonth - 1)//capability to start the screen with correct day in position
                //position parameter is currentDay - 1 because of the invisibleDay
//                var currentView : View = binding.recyclerView.layoutManager.findViewByPosition(LocalDateTime.now().dayOfMonth)!!
            }
        }.start()
    }

    fun displayDataForDate(time : LocalDate){
        var dayCalorie = 0
        var dayFat = 0
        var dayProtein = 0
        var dayCarb = 0
        var itemsLogged = 0
        binding.thisDateTV.text = time.format(formatters)
        dbRef.get().addOnSuccessListener { snapshot ->
            var foodSnapShot = snapshot.child(viewModel.year).child(time.month.toString()).child(
                time.dayOfMonth.toString()
            ).children
            for (foodItem in foodSnapShot) {
                dayCalorie += foodItem.child("calories").value.toString().toInt()
                dayFat = foodItem.child("fat").value.toString().toInt()
                dayProtein += foodItem.child("protein").value.toString().toInt()
                dayCarb += foodItem.child("carbohydrate").value.toString().toInt()
                itemsLogged++
            }

            binding.itemAmountLogged.text = "$itemsLogged items logged on this day"
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