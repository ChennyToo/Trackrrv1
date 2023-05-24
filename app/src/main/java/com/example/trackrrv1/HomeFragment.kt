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
                    R.id.homeButtonCalendar ->{
                        (activity as MainActivity?)!!.startTransition() //How to call methods in MainActivity
                        removeAllButtonFunctionality() //prevents the user from clicking once navigation starts
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            //TODO Navigate to Calendar Screen
                        }
                    }
                    R.id.homeButtonLogFood->{
                        (activity as MainActivity?)!!.startTransition()
                        removeAllButtonFunctionality()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController().navigate(R.id.action_homeFragment_to_mainFragment)
                        }
                    }
                }
            }
        binding.homeButtonLogFood.setOnClickListener(buttonsClickListener)
        binding.homeButtonCalendar.setOnClickListener(buttonsClickListener)


        return binding.root
    }

    fun removeAllButtonFunctionality(){
        //TODO add buttons as needed
        binding.homeButtonLogFood.setOnClickListener(null)
        binding.homeButtonCalendar.setOnClickListener(null)
    }

    fun calculateProgress(): Int {
        var calorie = viewModel.todayCalorie
        var progressOutOf100 = (calorie / 2000.0) * 100
        if (calorie > 2000) {
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

