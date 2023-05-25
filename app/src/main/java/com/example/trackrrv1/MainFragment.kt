package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.trackrrv1.databinding.FragmentMainBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import kotlin.properties.Delegates


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoodViewModel by activityViewModels()
    var isUp = false
    var firstClick = true
    lateinit var adapter : FoodAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animationView.frame = 0
        binding.animationView.pauseAnimation()
        Log.d("MainActivity", "ViewCreated")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("MainActivity", "CreatedView")
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        dbRef = Firebase.database.reference
        systemTime = viewModel.systemTime
        year = viewModel.year
        month = viewModel.month
        day = viewModel.day


        isUp = false
        firstClick = true
        binding.TakePhotoButton.hide()
        binding.WriteFoodButton.hide()


        var fabani : LottieAnimationView = binding.animationView
        fabani.loop(false)

        var testList2 : MutableList<Food> = mutableListOf(Food("Red", 2000, 30, 33, 44, 55, 66, systemTime),
            Food("Blue", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Purplepls", 20, 30, 33, 44, 55, 66, systemTime),
            Food("Pink", 20, 30, 33, 44, 55, 66, systemTime)
        )

        dbRef.child(year).child(month).child("25").child(testList2[0].foodName).setValue(testList2[0])
        dbRef.child(year).child(month).child("25").child(testList2[1].foodName).setValue(testList2[1])
        dbRef.child(year).child(month).child("25").child(testList2[2].foodName).setValue(testList2[2])
        dbRef.child(year).child(month).child("25").child(testList2[3].foodName).setValue(testList2[3])
        showFoodListToday()






        // Inflate the layout for this fragment
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.NewFoodButton -> {
                        if (firstClick) {
                            fabani.playAnimation()
                            firstClick = false
                            logFoodGUI()
                        } else {
                            fabani.reverseAnimationSpeed()
                            fabani.playAnimation()
                            logFoodGUI()
                        }

                    }
                    R.id.TakePhotoButton ->{
                        binding.root.findNavController().navigate(R.id.action_mainFragment_to_cameraFragment)
                    }

                    R.id.WriteFoodButton ->{
                        binding.root.findNavController().navigate(R.id.action_mainFragment_to_writeFragment)
                    }
                }
            }
        binding.NewFoodButton.setOnClickListener(buttonsClickListener)
        binding.TakePhotoButton.setOnClickListener(buttonsClickListener)
        binding.WriteFoodButton.setOnClickListener(buttonsClickListener)

        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("MainActivity", "CHANGED")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("MainActivity", "REMOVED")
                //TODO Should add showfoodlisttoday?
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        dbRef.child(year).child(month).child(day).addChildEventListener(childEventListener)
        refreshCheckerLoop()

        return binding.root    }

    fun logFoodGUI(){
        isUp = !isUp
        if(isUp){
            binding.TakePhotoButton.show()
            binding.WriteFoodButton.show()
        }
        else{
            binding.TakePhotoButton.hide()
            binding.WriteFoodButton.hide()
        }
    }

    fun refreshCheckerLoop() { //Checks if the screen needs to be refreshed for new foods
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                delay(500L)
                if (refreshScreen){
                    showFoodListToday()
                    refreshScreen = false
                    break
                }
            }
            this.cancel()//TODO may cause problems
        }
    }

    fun showFoodListToday(){
        val foodList = mutableListOf<Food>()
        dbRef.get().addOnSuccessListener{snapshot ->
            var foodSnapShot = snapshot.child(year).child(month).child(day).children
            for (foodItem in foodSnapShot){
                val name = foodItem.child("foodName").value.toString()
                val calorie = foodItem.child("calories").value.toString().toInt()
                val fat = foodItem.child("fat").value.toString().toInt()
                val sugar = foodItem.child("sugar").value.toString().toInt()
                val sodium = foodItem.child("sodium").value.toString().toInt()
                val protein = foodItem.child("protein").value.toString().toInt()
                val carbohydrate = foodItem.child("carbohydrate").value.toString().toInt()
                val imageUriString = foodItem.child("imageUriString").value.toString()
                val time = convertToTime(foodItem.child("timeLogged"))
                val newFood = Food(name?: "", calorie?: 0, fat?: 0, sugar?: 0,
                    sodium?: 0, protein?: 0, carbohydrate?: 0, time, imageUriString)
                foodList.add(newFood)
            }

            for (i in 0 until foodList.size - 1){
                for (j in 0 until foodList.size - 1 - i) {
                    if (foodList[j].timeLogged.compareTo(foodList[j + 1].timeLogged) > 0) {
                        val foodAtIndex = foodList[j]
                        foodList[j] = foodList[j + 1]
                        foodList[j + 1] = foodAtIndex
                    }
                }
            }

            adapter = FoodAdapter(foodList)
            Log.d("MainActivity", "before")


            binding.recyclerView.adapter = adapter
            (activity as MainActivity?)!!.endTransition()//starts the ending transition upon onCreateView
            //TODO May have to add condition to determine if transition plays
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.cancel()
        _binding = null
    }

    fun printfood(){
        Log.d("MainActivity", "possible")
    }


    fun convertToTime(foodItem : DataSnapshot) : LocalDateTime{
        val hour = (foodItem.child("hour").value as Long).toInt()
        val monthValue = (foodItem.child("monthValue").value as Long).toInt()
        val day = (foodItem.child("dayOfMonth").value as Long).toInt()
        val year = (foodItem.child("year").value as Long).toInt()
        val minute = (foodItem.child("minute").value as Long).toInt()
        val second = (foodItem.child("second").value as Long).toInt()
        return LocalDateTime.of(year, monthValue, day, hour, minute, second)
    }






    companion object {
        lateinit var dbRef: DatabaseReference
        var systemTime = LocalDateTime.now()
        var year = systemTime.year.toString()
        var month = systemTime.month.toString()
        var day = systemTime.dayOfMonth.toString()
        var refreshScreen = false

        fun removeItemInList(name : String){
            Log.d("MainActivity", "$name")
            dbRef.child(year).child(month).child(day).child(name).ref.removeValue()
        }


    }
}