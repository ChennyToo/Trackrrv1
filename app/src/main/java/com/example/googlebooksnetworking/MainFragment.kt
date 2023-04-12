package com.example.googlebooksnetworking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.googlebooksnetworking.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.getBooks()

        viewModel.response.observe(viewLifecycleOwner, Observer {bookList ->
            val adapter = BookAdapter(bookList)
            binding.recyclerView.adapter = adapter

        })
        // Inflate the layout for this fragment

        return binding.root    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}