package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentMainBinding
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        dbRef = Firebase.database.reference
//        dbRef.child("users").child("Meow").setValue(21)
        //Makes users branch
        // The expand for Meow:21
        systemTime = viewModel.systemTime
        viewModel._day.value = systemTime.dayOfMonth.toString()
        year = viewModel.year
        month = viewModel.month
        day = viewModel.day.value!!


        var testList : MutableList<Food> = mutableListOf(Food("Steak", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Vegetables", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Corndog", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Pork", 20, 30, 33, 44, 55, 66, systemTime)
        )

        var testList2 : MutableList<Food> = mutableListOf(Food("Red", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Blue", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Purple", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Pink", 20, 30, 33, 44, 55, 66, systemTime)
        )

        dbRef.child(year).child(month).child(day!!).child(testList2[0].foodName).setValue(testList2[0])
        dbRef.child(year).child(month).child(day!!).child(testList2[1].foodName).setValue(testList2[1])
        dbRef.child(year).child(month).child(day!!).child(testList2[2].foodName).setValue(testList2[2])
        dbRef.child(year).child(month).child(day!!).child(testList2[3].foodName).setValue(testList2[3])
        getFoodListDay(30)

        viewModel.response.observe(viewLifecycleOwner, Observer {foodList ->
            val adapter = FoodAdapter(foodList)
            binding.recyclerView.adapter = adapter

        })


        viewModel.day.observe(viewLifecycleOwner, Observer{
            viewModel.currentFoodNumber = 0
        })

        // Inflate the layout for this fragment
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.NewFoodButton -> {
                        binding.root.findNavController().navigate(R.id.action_mainFragment_to_cameraFragment)
                    }
                }
            }
        binding.NewFoodButton.setOnClickListener(buttonsClickListener)


        return binding.root    }

    fun getFoodListDay(day : Int){
        val foodList = mutableListOf<Food>()
        dbRef.get().addOnSuccessListener{snapshot ->
            var foodSnapShot = snapshot.child(year).child(month).child(day.toString()).children
            for (foodItem in foodSnapShot){
                val name = foodItem.child("foodName").value.toString()
                val calorie = foodItem.child("calories").value.toString().toInt()
                val fat = foodItem.child("fat").value.toString().toInt()
                val sugar = foodItem.child("sugar").value.toString().toInt()
                val sodium = foodItem.child("sodium").value.toString().toInt()
                val protein = foodItem.child("protein").value.toString().toInt()
                val carbohydrate = foodItem.child("carbohydrate").value.toString().toInt()
                val imageUriString = foodItem.child("imageUriString").value.toString()
                val newFood = Food(name?: "", calorie?: 0, fat?: 0, sugar?: 0,
                    sodium?: 0, protein?: 0, carbohydrate?: 0, LocalDateTime.now(), imageUriString)
                foodList.add(newFood)
            }
            val adapter = FoodAdapter(foodList)
            binding.recyclerView.adapter = adapter

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        lateinit var dbRef: DatabaseReference
        var systemTime = LocalDateTime.now()
        var year = systemTime.year.toString()
        var month = systemTime.month.toString()
        var day = systemTime.dayOfMonth.toString()
        fun removeItemInList(name : String){
            var foodSnapShot = dbRef.child(year).child(month).child(day).child(name).ref.removeValue()
        }
    }
}