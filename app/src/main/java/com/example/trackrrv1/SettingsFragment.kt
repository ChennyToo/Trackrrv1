package com.example.trackrrv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentSettingsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        populateButtonsFunctionality()




        (activity as MainActivity?)!!.endTransition()
        return binding.root    }

    fun populateButtonsFunctionality(){
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.settingsHomeScreenButton -> {
                        (activity as MainActivity?)!!.startTransition()
                        removeAllButtonFunctionality()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_settingsFragment_to_homeFragment)
                        }
                    }

                    R.id.settingsCustomizeHomeButton ->{
                        (activity as MainActivity?)!!.startTransition()
                        removeAllButtonFunctionality()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_settingsFragment_to_settingsCustomizeHomeFragment)
                        }
                    }
                }
            }

        binding.settingsHomeScreenButton.setOnClickListener(buttonsClickListener)
        binding.settingsCustomizeHomeButton.setOnClickListener(buttonsClickListener)
        binding.settingsCustomizeFoodListButton.setOnClickListener(buttonsClickListener)
    }

    private fun removeAllButtonFunctionality(){
        binding.settingsHomeScreenButton.isClickable = false

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}