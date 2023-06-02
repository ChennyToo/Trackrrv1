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
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
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
        binding.goalsValueDisplay.text = editableCalorie.toString()
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
        (activity as MainActivity?)!!.endTransition()
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
                binding.goalsValueDisplay.text = editableCalorie.toString()
            }

            1 -> {
                binding.goalsNutrientLabel.text = "Protein(g)"
                binding.goalsValueDisplay.text = editableProtein.toString()
            }

            2 -> {
                binding.goalsNutrientLabel.text = "Carbohydrates(g)"
                binding.goalsValueDisplay.text = editableCarb.toString()
            }

            3 -> {
                binding.goalsNutrientLabel.text = "Sodium(mg)"
                binding.goalsValueDisplay.text = editableSodium.toString()
            }

            4 -> {
                binding.goalsNutrientLabel.text = "Fat(g)"
                binding.goalsValueDisplay.text = editableFat.toString()
            }

            5 -> {
                binding.goalsNutrientLabel.text = "Sugar(g)"
                binding.goalsValueDisplay.text = editableSugar.toString()
            }
        }


    }

    private fun initializeButtons() {
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.goalsPlusButton -> {
                        changeNutrientGoalValue(1)
                    }

                    R.id.goalsMinusButton -> {
                        changeNutrientGoalValue(-1)
                    }

                    R.id.goalsHomeButton -> {
                        binding.goalsHomeButton.isClickable = false
                        (activity as MainActivity?)!!.startTransition()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_goalsFragment_to_homeFragment)
                        }
                    }
                }
            }
        binding.goalsPlusButton.setOnClickListener(buttonsClickListener)
        binding.goalsMinusButton.setOnClickListener(buttonsClickListener)
        binding.goalsHomeButton.setOnClickListener(buttonsClickListener)
    }

    private fun changeNutrientGoalValue(isAdding: Int) {
        when (currentPagerPosition) {
            0 -> {
                if (editableCalorie > 100 || isAdding > 0) {
                    editableCalorie += (100 * isAdding)
                    binding.goalsValueDisplay.text = editableCalorie.toString()
                }

                else {
                    Toast.makeText(requireActivity(), "Can't go lower!", Toast.LENGTH_SHORT).show()
                }


            }

            1 -> {
                if (editableProtein > 5 || isAdding > 0) {
                    editableProtein += (5 * isAdding)
                    binding.goalsValueDisplay.text = editableProtein.toString()
                }

                else {
                    Toast.makeText(requireActivity(), "Can't go lower!", Toast.LENGTH_SHORT).show()
                }


            }

            2 -> {
                if (editableCarb > 5 || isAdding > 0) {
                    editableCarb += (5 * isAdding)
                    binding.goalsValueDisplay.text = editableCarb.toString()
                }

                else {
                    Toast.makeText(requireActivity(), "Can't go lower!", Toast.LENGTH_SHORT).show()
                }


            }

            3 -> {
                if (editableSodium > 100 || isAdding > 0) {
                    editableSodium += (100 * isAdding)
                    binding.goalsValueDisplay.text = editableSodium.toString()
                }

                else {
                    Toast.makeText(requireActivity(), "Can't go lower!", Toast.LENGTH_SHORT).show()
                }


            }

            4 -> {
                if (editableFat > 5 || isAdding > 0) {
                    editableFat += (5 * isAdding)
                    binding.goalsValueDisplay.text = editableFat.toString()
                }

                else {
                    Toast.makeText(requireActivity(), "Can't go lower!", Toast.LENGTH_SHORT).show()
                }


            }

            5 -> {
                if (editableSugar > 5 || isAdding > 0) {
                    editableSugar += (5 * isAdding)
                    binding.goalsValueDisplay.text = editableSugar.toString()
                }

                else {
                    Toast.makeText(requireActivity(), "Can't go lower!", Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    override fun onStop() {//Upon the Fragment getting exited out via navigation or phone turn off, it will save data to Shared Preferences
        super.onStop()
        Constants.calorieIntake = editableCalorie
        Constants.proteinIntake = editableProtein
        Constants.carbIntake = editableCarb
        Constants.sodiumIntake = editableSodium
        Constants.fatIntake = editableFat
        Constants.sugarIntake = editableSugar

        var sharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt("calorieIntake", editableCalorie)
        editor.putInt("proteinIntake", editableProtein)
        editor.putInt("carbIntake", editableCarb)
        editor.putInt("sodiumIntake", editableSodium)
        editor.putInt("fatIntake", editableFat)
        editor.putInt("sugarIntake", editableSugar)
        editor.commit()
    }
}