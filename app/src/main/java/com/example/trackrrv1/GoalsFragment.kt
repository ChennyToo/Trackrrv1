package com.example.trackrrv1

import android.net.SocketKeepalive.Callback
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
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
        val images = listOf(R.drawable.cal_bread, R.drawable.cal_butter, R.drawable.cal_meat, R.drawable.carrot)
        val adapter = ViewPagerAdapter(images)
        binding.goalsViewPager.adapter = adapter
        val pager : ViewPager2 = binding.goalsViewPager

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                //Everytime user scrolls, we can see where the position of the ViewPager we are in
                Log.d("GoalsFragment", "${position}")
                super.onPageSelected(position)
            }


        })
        // Inflate the layout for this fragment
        return binding.root
    }



}