package com.example.trackrrv1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.trackrrv1.databinding.FragmentLogInBinding

class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LogInViewModel by activityViewModels()
    lateinit var sharedPref : SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        var editor : SharedPreferences.Editor = sharedPref.edit()

        binding.loginEnterButton.setOnClickListener {
            editor.putString("username", binding.loginUsernameField.text.toString())
            editor.commit()
        }





        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}