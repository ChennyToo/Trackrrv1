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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()
    lateinit var dbRef: DatabaseReference

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

                    R.id.DeleteFoodItemButton ->{
                        //ADD CODE HERE
                    }
                }
            }
        binding.NewFoodButton.setOnClickListener(buttonsClickListener)
        binding.DeleteFoodItemButton.set




        return binding.root    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}