package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentWriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class WriteFragment : Fragment() {
    private val foodArgs by navArgs<WriteFragmentArgs>()

    lateinit var dbRef: DatabaseReference
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()
    private var isEditState = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isEditState = false
//        Log.d("Main", "${foodArgs.foodItemPassedInFromEdit}")


        dbRef = Firebase.database.reference.child(Constants.username)
        _binding = FragmentWriteBinding.inflate(inflater, container, false)

        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.LogButton -> {
                        checkFieldValidity()
                    }

                    R.id.writeBackScreenButton -> {
                        disableButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_writeFragment_to_mainFragment)
                        }
                    }
                }
            }
        binding.LogButton.setOnClickListener(buttonsClickListener)
        binding.writeBackScreenButton.setOnClickListener(buttonsClickListener)
        if (foodArgs.foodItemPassedInFromEdit.foodName != "empty") {
            isEditState = true
            changeToEditState(foodArgs.foodItemPassedInFromEdit)
        }
        (activity as MainActivity?)!!.endTransition()//finish the transition after everything else is done

        // Inflate the layout for this fragment
        return binding.root
    }

    fun LogAndNavigate() {
        val name = binding.NameEdit.text.toString()
        val calorie = if (binding.CalorieEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.CalorieEdit.text.toString().toInt()
        }
        val protein = if (binding.ProteinEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.ProteinEdit.text.toString().toInt()
        }
        val carb = if (binding.CarbEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.CarbEdit.text.toString().toInt()
        }
        val fat = if (binding.FatEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.FatEdit.text.toString().toInt()
        }
        val sugar = if (binding.SugarEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.SugarEdit.text.toString().toInt()
        }
        val imageUrl = "https://www.iconsdb.com/icons/preview/red/x-mark-3-xxl.png"
        val time = if (isEditState) {
            foodArgs.foodItemPassedInFromEdit.timeLogged
        } else {
            LocalDateTime.now()
        }
        val newFood = Food(
            name ?: "", calorie ?: 0, fat ?: 0, sugar,
            0, protein ?: 0, carb ?: 0, time, imageUrl
        )
        if (isEditState) {
            //TODO FIND HOW TO UPDATE
//            dbRef.child(LocalDateTime.now().year.toString())
//                .child(LocalDateTime.now().month.toString())
//                .child(foodArgs.foodItemPassedInFromEdit.timeLogged.dayOfMonth.toString())
//                .child(foodArgs.foodItemPassedInFromEdit.foodName).updateChildren(
//                    newFood.toMap()).addOnSuccessListener {
//                    MainFragment.refreshScreen = true
//                }

            dbRef.child(LocalDateTime.now().year.toString())
                .child(LocalDateTime.now().month.toString())
                .child(foodArgs.foodItemPassedInFromEdit.timeLogged.dayOfMonth.toString())//Editing a food item while changing days will update yesterday's date
                .child(foodArgs.foodItemPassedInFromEdit.foodName)
                .ref.removeValue()

            Log.d("mew", "${foodArgs.foodItemPassedInFromEdit.foodName}")
            dbRef.child(LocalDateTime.now().year.toString())
                .child(LocalDateTime.now().month.toString())
                .child(foodArgs.foodItemPassedInFromEdit.timeLogged.dayOfMonth.toString())
                .child(name).setValue(newFood).addOnSuccessListener {
                    MainFragment.refreshScreen = true
                }


        } else {
            dbRef.child(LocalDateTime.now().year.toString())
                .child(LocalDateTime.now().month.toString())
                .child(LocalDateTime.now().dayOfMonth.toString())
                .child(name!!).setValue(newFood).addOnSuccessListener {
                    MainFragment.refreshScreen = true
                }
        }
        (activity as MainActivity?)!!.startTransition()
        lifecycleScope.launch() {
            delay(Constants.transitionStartTime)
            binding.root.findNavController().navigate(R.id.action_writeFragment_to_mainFragment)
        }

    }

    fun changeToEditState(foodItem: Food) {
        binding.NameEdit.setText(foodItem.foodName)
        binding.CalorieEdit.setText(foodItem.calories.toString())
        binding.ProteinEdit.setText(foodItem.protein.toString())
        binding.CarbEdit.setText(foodItem.carbohydrate.toString())
        binding.FatEdit.setText(foodItem.fat.toString())
    }

    fun checkFieldValidity() {
        val name = binding.NameEdit.text.toString()
        if (name.contains(".")
            || name.contains("#")
            || name.contains("$")
            || name.contains("[")
            || name.contains("]")
            || name.isEmpty()
        ) {
            displayErrorToUser()
        } else {
            disableButtonFunctionality()
            LogAndNavigate()
        }
    }

    fun displayErrorToUser() {
        Log.d("WriteFragment", "displayErrorToUser name field has bad characters")
    }

    fun disableButtonFunctionality(){
        binding.writeBackScreenButton.isClickable = false
        binding.LogButton.isClickable = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}