package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentCalendarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    lateinit var Thread1 : Job //Each thread is meant to animate the nutrition progress bar
    lateinit var Thread2 : Job
    lateinit var Thread3 : Job
    var didProgressThreadsCreated = false //shows if threads have been created yet

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)!!.endTransition()
        Log.d("MainActvity", "CREATEDCAL")
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        didProgressThreadsCreated = false
        density = getResources().getDisplayMetrics().density
        dbRef = Firebase.database.reference
        displayDataForDate(now)
        displayDaysOfMonth(now.monthValue)

        var initializationCheck = 0

        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
            when (view.id) {
                R.id.homeScreenButton ->{
                    removeAllButtonFunctionality()
                    (activity as MainActivity?)!!.startTransition() //How to call methods in MainActivity
//                    removeAllButtonFunctionality() //prevents the user from clicking once navigation starts
                    lifecycleScope.launch() {
                        delay(Constants.transitionStartTime)
                        binding.root.findNavController().navigate(R.id.action_calendarFragment_to_homeFragment)
                    }
                }
            }
        }

        binding.homeScreenButton.setOnClickListener(buttonsClickListener)


        var monthAdapter = MonthAdapter(this, Constants.monthList)

        Thread {//lessen system load
            activity?.runOnUiThread{
                binding.monthSpinner.adapter = monthAdapter
                binding.monthSpinner.setPopupBackgroundResource(R.drawable.cal_spinnerbg) //sets the background
                binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(initializationCheck !=0) {
                            displayDaysOfMonth(position + 1)//index starts at 0, month value starts at 1
                            binding.selectedMonthTV.text = Constants.monthList.get(position)
                        }
                        initializationCheck++
                    }

                }
            }
        }.start()

        // Inflate the layout for this fragment
        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.cancel()
        DayAdapter.startScreen = true
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
        if(didProgressThreadsCreated){ //This is to stop the threads from animating once day changes
            Thread1.cancel() //Threads start after Firebase data is obtained
            Thread2.cancel() //Therefore, trying to cancel threads before creation will result in crash
            Thread3.cancel() //Only cancel threads that have already been created via if statement
        }
            binding.proteinProgress.progress = 0 //reset all progress bars to 0 once day changes
            binding.carbProgress.progress = 0 //animation will play soon after
            binding.fatProgress.progress = 0

        var dayCalorie = 0
        var dayFat = 0
        var dayProtein = 0
        var dayCarb = 0
        var itemsLogged = 0
        binding.selectedMonthTV.text = Constants.monthList.get(time.monthValue - 1)
        binding.thisMonthTV.text = Constants.monthList.get(time.monthValue - 1)
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

            var proProgress = ((dayProtein * 100 / Constants.proteinIntake)) //where the progress meter will end
            var carbProgress = ((dayCarb * 100 / Constants.carbIntake))
            var fatProgress = ((dayFat * 100 / Constants.fatIntake))
            if(proProgress > 100){//if progress is above 100 due to overconsumption
                proProgress = 100//change to 100 as that will affect animation speed
            }
            if (carbProgress > 100){
                carbProgress = 100
            }
            if (fatProgress > 100){
                fatProgress = 100
            }

            Thread1 = lifecycleScope.launch() {//Thread to animate progress bar
                var progress = 0
                var frequency = 1L
                if (proProgress != 0){
                    frequency = (Constants.calProgressAnimationDuration / proProgress).toLong()
                }
                while (progress <= proProgress) {
                    binding.proteinProgress.progress = progress
                    delay(frequency)
                    progress++
                }
            }

                Thread2 = lifecycleScope.launch() {
                    var progress = 0
                    var frequency = 1L
                    if (proProgress != 0){
                        frequency = (Constants.calProgressAnimationDuration / carbProgress).toLong()
                    }
                    while (progress <= carbProgress) {
                        binding.carbProgress.progress = progress
                        delay(frequency)
                        progress++
                    }
                }


            Thread3 = lifecycleScope.launch(){
                var progress = 0
                var frequency = 1L
                if (proProgress != 0){
                    frequency = (Constants.calProgressAnimationDuration / fatProgress).toLong()
                }
                while (progress <= fatProgress){
                    binding.fatProgress.progress = progress
                    delay(frequency)
                    progress++
                }
            }

            didProgressThreadsCreated = true





        }
    }//TODO TEST IF WORKS LATER

    fun removeAllButtonFunctionality(){//When navigating the user doesn't have the ability to access buttons
        binding.recyclerView.isClickable = false
        binding.homeScreenButton.isClickable = false
        binding.monthSpinner.isClickable = false
    }




    companion object{
        var density : Float = 0f
    }


}