package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.trackrrv1.databinding.FragmentHomeBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                lifecycleScope.cancel()
            }


            val buttonsClickListener: View.OnClickListener =
                View.OnClickListener { view ->
                    when (view.id) {



                    }
                }



            return binding.root
        }

    fun calculateProgress() : Int{
        var calorie = viewModel.todayCalorie
        var progressOutOf100 = (calorie / 2000.0) * 100
        if (calorie > 2000){
            progressOutOf100 = 100.0
        }

        var progressOfCircle = (progressOutOf100 * .75).toInt()
        return progressOfCircle
    }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    companion object{
        var checkCalories = false
     }
    }

