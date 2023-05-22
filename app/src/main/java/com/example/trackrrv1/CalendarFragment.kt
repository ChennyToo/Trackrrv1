package com.example.trackrrv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trackrrv1.databinding.FragmentCalendarBinding


class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val days = mutableListOf(Day("Mo", 1),
            Day("Tu", 2),
            Day("We", 3),
            Day("Th", 4),
            Day("Fr", 5),
            Day("Sa", 6),
            Day("Su", 7)
        )

        val mAdapter = DayAdapter(days)
        binding.recyclerView.adapter = mAdapter

        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}