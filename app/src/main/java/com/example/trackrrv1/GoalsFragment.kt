package com.example.trackrrv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trackrrv1.databinding.FragmentGoalsBinding
import com.example.trackrrv1.databinding.FragmentLogInBinding

class GoalsFragment : Fragment() {
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }


}