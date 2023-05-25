package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentHomeBinding
import kotlinx.coroutines.Delay
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.concurrent.thread

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getCalorieToday()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        lifecycleScope.launch() {
            populateFields()
        }
        (activity as MainActivity?)!!.endTransition()
        lifecycleScope.launch {
            while (!checkCalories) {
                delay(10L) //repeated checking of when viewModel finishes calculations
            }
            checkCalories = false
            binding.calorieCount.text = viewModel.todayCalorie.toString()//set calories for textView
            var progress = 0
            delay(500L)//delay is so that the animation does not play too early
            while (progress < calculateProgress()) {//progress of 74 is max circle
                delay(13L) //Increased frequency, the faster the animation
                binding.circularProgressIndicator.progress = progress
                progress++
            }
            this.cancel()
        }

        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.homeButtonCalendar -> {
                        (activity as MainActivity?)!!.startTransition() //How to call methods in MainActivity
                        removeAllButtonFunctionality() //prevents the user from clicking once navigation starts
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_homeFragment_to_calendarFragment)
                        }
                    }

                    R.id.homeButtonLogFood -> {
                        (activity as MainActivity?)!!.startTransition()
                        removeAllButtonFunctionality()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_homeFragment_to_mainFragment)
                        }
                    }
                }
            }
        binding.homeButtonLogFood.setOnClickListener(buttonsClickListener)
        binding.homeButtonCalendar.setOnClickListener(buttonsClickListener)


        return binding.root
    }

    fun removeAllButtonFunctionality() {
        //TODO add buttons as needed
        binding.homeButtonLogFood.setOnClickListener(null)
        binding.homeButtonCalendar.setOnClickListener(null)
    }

    fun populateFields() {
        //TODO Maybe able to have only one observer?
        //Possible to have one observer by having the third field being observed and then
        //update fields 1 + 2 first as those values have already been calculated
        viewModel.field1value.observe(viewLifecycleOwner) { updatedValue ->
            Log.d("MainActivity", "Calling1")
            var text = Constants.HCField1Type.substring(0, 1)
                .uppercase(Locale.ROOT) + Constants.HCField1Type.substring(
                1,
                Constants.HCField1Type.length
            )
            binding.redBoxTitle.text = if (text != "Carbohydrate") {
                text
            } else {
                "Carb"
            }
            binding.redBoxContent.text =
                "$updatedValue${getUnit(Constants.HCField1Type)}"//TODO implement unit
        }
        viewModel.field2value.observe(viewLifecycleOwner) { updatedValue ->
            Log.d("MainActivity", "Calling2")
            var text2 = Constants.HCField2Type.substring(0, 1)
                .uppercase(Locale.ROOT) + Constants.HCField2Type.substring(
                1,
                Constants.HCField2Type.length
            )
            binding.purpleBoxTitle.text = if (text2 != "Carbohydrate") {
                text2
            } else {
                "Carb"
            }
            binding.purpleBoxContent.text =
                "${updatedValue}${getUnit(Constants.HCField2Type)}"//TODO implement unit
        }

        viewModel.field3value.observe(viewLifecycleOwner) { updatedValue ->
            Log.d("MainActivity", "Calling3")
            var text3 = Constants.HCField3Type.substring(0, 1)
                .uppercase(Locale.ROOT) + Constants.HCField3Type.substring(
                1,
                Constants.HCField3Type.length
            )
            binding.cyanBoxTitle.text = if (text3 != "Carbohydrate") {
                text3
            } else {
                "Carb"
            }
            binding.cyanBoxContent.text =
                "${updatedValue}${getUnit(Constants.HCField3Type)}"//TODO implement unit
        }

        //break line
        viewModel.getNutritionToday(Constants.HCField1Type)
        viewModel.getNutritionToday(Constants.HCField2Type)
        viewModel.getNutritionToday(Constants.HCField3Type)

    }

    fun getUnit(nutritionName: String): String {
        if (nutritionName.equals("sodium")) {
            return "mg"
        } else if (nutritionName.equals("protein") ||
            nutritionName.equals("sugar") ||
            nutritionName.equals("fat") ||
            nutritionName.equals("carbohydrate")
        ) {
            return "g"
        }

        return "error"


    }


    fun calculateProgress(): Int {
        var calorie = viewModel.todayCalorie
        var progressOutOf100 = (calorie / Constants.calorieIntake.toDouble()) * 100
        if (calorie > Constants.calorieIntake) {
            progressOutOf100 = 100.0
        }

        var progressOfCircle = (progressOutOf100 * .75).toInt()
        return progressOfCircle
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        lifecycleScope.cancel()
    }

    companion object {
        var checkCalories = false
    }
}

