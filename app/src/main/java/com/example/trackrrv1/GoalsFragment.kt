package com.example.trackrrv1

import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.net.SocketKeepalive.Callback
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.trackrrv1.databinding.FragmentGoalsBinding
import com.example.trackrrv1.databinding.FragmentLogInBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GoalsFragment : Fragment() {
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!
    var currentPagerPosition = 0
    var isObjectAnimatorPlaying = false
    var editableCalorie = Constants.calorieIntake
    var editableProtein = Constants.proteinIntake
    var editableCarb = Constants.carbIntake
    var editableSodium = Constants.sodiumIntake
    var editableFat = Constants.fatIntake
    var editableSugar = Constants.sugarIntake

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        val images = listOf(
            R.drawable.goals_caloriecard, R.drawable.goals_proteincard,
            R.drawable.goals_carbcard, R.drawable.goals_saltcard,
            R.drawable.goals_fatcard, R.drawable.goals_sugarcard
        )

        initializeButtons()
        val adapter = ViewPagerAdapter(images)
        binding.goalsViewPager.adapter = adapter
        val pager: ViewPager2 = binding.goalsViewPager

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {//If user starts to slightly move the image, it will call the function with state of 1
                //When user stops moving, it will be called again with state of 2
                //When image stops on screen, it will be called again with state of 0
                Log.d("GoalsFragment", "State: ${state}")
                if (state == 1) {
                    binding.goalsNutrientLabel.text = ""
                    if (!isObjectAnimatorPlaying) {
                        ObjectAnimator.ofFloat(binding.goalsWholeBottomScreen, "translationY", 800f)
                            .apply {
                                duration = 400
                                start()
                                isObjectAnimatorPlaying = true
                            }
                    }
                    lifecycleScope.launch {
                        delay(400L)
                        isObjectAnimatorPlaying = false
                    }

                } else if (state == 0) {
                    updateBottomScreenWithFood()
                }


                super.onPageScrollStateChanged(state)
            }

            override fun onPageSelected(position: Int) {
                //Everytime user scrolls, we can see where the position of the ViewPager we are in
                Log.d("GoalsFragment", "Position: ${position}")
                currentPagerPosition = position
                super.onPageSelected(position)
            }


        })
        // Inflate the layout for this fragment
        return binding.root
    }

    fun updateBottomScreenWithFood() {
        ObjectAnimator.ofFloat(binding.goalsWholeBottomScreen, "translationY", 0f)
            .apply {
                duration = 400
                start()
                isObjectAnimatorPlaying = true
                Log.d("GoalsFragment", "Going up")
            }
        lifecycleScope.launch {
            delay(400L)
            isObjectAnimatorPlaying = false
        }

        when (currentPagerPosition) {
            0 -> {
                binding.goalsNutrientLabel.text = "Calories"
                binding.goalsValueDisplay.text = "Cal"
            }

            1 -> {
                binding.goalsNutrientLabel.text = "Protein"
                binding.goalsValueDisplay.text = "Pro"
            }

            2 -> {
                binding.goalsNutrientLabel.text = "Carbohydrates"
                binding.goalsValueDisplay.text = "Carb"
            }

            3 -> {
                binding.goalsNutrientLabel.text = "Sodium"
                binding.goalsValueDisplay.text = "Salt"
            }

            4 -> {
                binding.goalsNutrientLabel.text = "Fat"
                binding.goalsValueDisplay.text = "Fat"
            }

            5 -> {
                binding.goalsNutrientLabel.text = "Sugar"
                binding.goalsValueDisplay.text = "Sugar"
            }
        }


    }

    private fun initializeButtons(){
        val buttonsClickListener: View.OnClickListener =
        View.OnClickListener { view ->
            when (view.id) {
                R.id.goalsPlusButton ->{changeNutrientGoalValue(1)}
                R.id.goalsMinusButton ->{changeNutrientGoalValue(-1)}

            }
        }
        binding.goalsPlusButton.setOnClickListener(buttonsClickListener)
        binding.goalsMinusButton.setOnClickListener(buttonsClickListener)
    }

    private fun changeNutrientGoalValue(isAdding : Int){
        when(currentPagerPosition){
            0 ->{
                editableCalorie += (100 * isAdding)
                binding.goalsValueDisplay.text = editableCalorie.toString()
            }
            1 ->{
                editableProtein += (5 * isAdding)
                binding.goalsValueDisplay.text = editableProtein.toString()
            }

            2 ->{
                editableCarb += (5 * isAdding)
                binding.goalsValueDisplay.text = editableCarb.toString()
            }

            3 ->{
                editableSodium += (100 * isAdding)
                binding.goalsValueDisplay.text = editableSodium.toString()
            }

            4 -> {
                editableFat += (5 * isAdding)
                binding.goalsValueDisplay.text = editableFat.toString()

            }

            5 ->{
                editableSugar += (5 * isAdding)
                binding.goalsValueDisplay.text = editableSugar.toString()
            }
        }

    }

    override fun onStop() {//Upon the Fragment getting exited out via navigation or phone turn off, it will save data to Shared Preferences
        super.onStop()
    //                var sharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
//                var editor: SharedPreferences.Editor = sharedPref.edit()
//                editor.putString("HCField1", Position1Value)
//                editor.putString("HCField2", Position2Value)
//                editor.putString("HCField3", Position3Value)
//                editor.commit()
    }
}