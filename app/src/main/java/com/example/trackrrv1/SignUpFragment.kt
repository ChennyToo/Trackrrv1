package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentSignUpBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var dbRef = Firebase.database.reference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        binding.signupButton.setOnClickListener {
            val username = if (binding.signupNamefield.text.toString().isEmpty()) {
                "empty"
            } else {
                binding.signupNamefield.text.toString()
            }
            val password = if (binding.signupPasswordField.text.toString().isEmpty()) {
                "empty"
            } else {
                binding.signupPasswordField.text.toString()
            }
            val confirmpassword =
                if (binding.signupConfirmPasswordField.text.toString().isEmpty()) {
                    "empty"
                } else {
                    binding.signupConfirmPasswordField.text.toString()
                }
            dbRef.get().addOnSuccessListener { snapshot ->
                var credentialSnapShot = snapshot.child(username).value
                if (credentialSnapShot != null){
                    Toast.makeText(requireActivity(), "Username taken!", Toast.LENGTH_SHORT).show()
                }

                else {
                    if (password != confirmpassword){
                        Toast.makeText(requireActivity(), "Passwords don't match!", Toast.LENGTH_SHORT).show()
                    }

                    else {
                        if (password == "empty" || username == "empty"){
                            Toast.makeText(requireActivity(), "Don't leave fields blank!", Toast.LENGTH_SHORT).show()
                        }

                        else {
                            dbRef.child(username).child("Password").setValue(password)
                            disableButtonFunctionality()
                            (activity as MainActivity?)!!.startTransition()//begins screen transition
                            lifecycleScope.launch {
                                delay(Constants.transitionStartTime)
                                binding.root.findNavController()
                                    .navigate(R.id.action_signUpFragment_to_logInFragment)
                            }
                            Toast.makeText(requireActivity(), "Account made!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }


        }

        binding.signupBackButton.setOnClickListener {
            disableButtonFunctionality()
            (activity as MainActivity?)!!.startTransition()//begins screen transition
            lifecycleScope.launch {
                delay(Constants.transitionStartTime)
                binding.root.findNavController()
                    .navigate(R.id.action_signUpFragment_to_logInFragment)
            }
        }

        (activity as MainActivity?)!!.endTransition()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun disableButtonFunctionality(){
        binding.signupButton.isClickable = false
        binding.signupBackButton.isClickable = false
    }


}