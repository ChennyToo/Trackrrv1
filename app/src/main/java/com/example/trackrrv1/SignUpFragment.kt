package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentSignUpBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
            val username = if (binding.signupNamefield.text.toString().isEmpty()){"empty"} else {binding.signupNamefield.text.toString()}
            val password = binding.signupPasswordField.text.toString()
            dbRef.get().addOnSuccessListener { snapshot ->
                var credentialSnapShot = snapshot.child(username).value
                if (credentialSnapShot == null){//Is name is not taken yet
                    dbRef.child(username).child("Password").setValue(password)
                    binding.root.findNavController()
                        .navigate(R.id.action_signUpFragment_to_logInFragment)
                }

                else {//If name exists
                    Log.d("LogIn", "Name already exists")
                }
            }



        }

        // Inflate the layout for this fragment
        return binding.root
    }


}