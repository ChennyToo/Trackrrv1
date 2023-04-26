package com.example.trackrrv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
//        viewModel.getFoods() //This is the API Call

        viewModel.response.observe(viewLifecycleOwner, Observer {foodList ->
            val adapter = FoodAdapter(foodList)
            binding.recyclerView.adapter = adapter

        })
        // Inflate the layout for this fragment
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.NewFoodButton -> {
                        binding.root.findNavController().navigate(R.id.action_mainFragment_to_cameraFragment)
                    }
                }
            }
        binding.NewFoodButton.setOnClickListener(buttonsClickListener)


        return binding.root    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}