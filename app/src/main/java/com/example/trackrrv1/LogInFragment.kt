package com.example.trackrrv1

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogInFragment : Fragment() {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LogInViewModel by activityViewModels()
    lateinit var sharedPref: SharedPreferences
    private var dbRef = Firebase.database.reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        sharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPref.edit()
        var loginSound = MediaPlayer.create(requireContext(), R.raw.write_logfoodsound)

        binding.loginEnterButton.setOnClickListener {
            val loginUserCredential = binding.loginUsernameField.text.toString()
            val loginPasswordCredential = binding.loginPasswordField.text.toString()
            dbRef.get().addOnSuccessListener { snapshot ->
                var credentialSnapShot = snapshot.child(loginUserCredential).child("Password").value
                if (credentialSnapShot == loginPasswordCredential) {
                    disableButtonFunctionality()
                    loginSound.start()
                    editor.putString("username", binding.loginUsernameField.text.toString())
                    editor.commit()
                    Constants.username = loginUserCredential
                    Constants.userDatabaseReference = Firebase.database.reference.child(Constants.username)
                    (activity as MainActivity?)!!.startTransition()//begins screen transition
                    lifecycleScope.launch {
                        delay(Constants.transitionStartTime)
                        binding.root.findNavController()
                            .navigate(R.id.action_logInFragment_to_homeFragment)
                    }
                } else {
                    //TODO Tell user that password or username is not valid
                    Log.d("LogIn", "Invalid credentials")
                    Toast.makeText(requireActivity(), "Wrong password or username...", Toast.LENGTH_SHORT).show()
                }


            }
        }

        binding.loginSignUpButton.setOnClickListener {
            disableButtonFunctionality()
            (activity as MainActivity?)!!.startTransition()//begins screen transition
            lifecycleScope.launch {
                delay(Constants.transitionStartTime)
                binding.root.findNavController()
                    .navigate(R.id.action_logInFragment_to_signUpFragment)
            }
        }

        (activity as MainActivity?)!!.endTransition()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun disableButtonFunctionality(){
        binding.loginEnterButton.isClickable = false
        binding.loginSignUpButton.isClickable = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}