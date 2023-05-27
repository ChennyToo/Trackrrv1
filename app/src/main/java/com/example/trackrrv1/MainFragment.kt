package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
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
    val binding get() = _binding!!

    private val viewModel: FoodViewModel by activityViewModels()
    var isUp = false
    var firstClick = true
    lateinit var foodList: MutableList<Food>
    lateinit var adapter: FoodAdapter
    var foodListSize = 0

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
        var previouslyClickedIcon = binding.mainButtonAll


        var fabani: LottieAnimationView = binding.animationView
        fabani.loop(false)
        var timee = LocalDateTime.of(2023, 5, 27, 4, 30)
        var timee2 = LocalDateTime.of(2023, 5, 27, 6, 30)
        var timee3 = LocalDateTime.of(2023, 5, 27, 8, 30)
        var timee4 = LocalDateTime.of(2023, 5, 27, 10, 30)
        var timee5 = LocalDateTime.of(2023, 5, 27, 12, 30)
        var timee6 = LocalDateTime.of(2023, 5, 27, 14, 30)
        var timee7 = LocalDateTime.of(2023, 5, 27, 16, 30)
        var timee8 = LocalDateTime.of(2023, 5, 27, 18, 30)
        var timee9 = LocalDateTime.of(2023, 5, 27, 20, 30)
        var timee10 = LocalDateTime.of(2023, 5, 27, 22, 30)
        var timee11 = LocalDateTime.of(2023, 5, 27, 24, 30)

        var testList2: MutableList<Food> = mutableListOf(
            Food("A", 2000, 30, 33, 44, 55, 66, timee),
            Food("B", 20, 30, 33, 44, 55, 66, timee2),
            Food("C", 20, 30, 33, 44, 55, 66, timee3),
            Food("D", 20, 30, 33, 44, 55, 66, timee4),
            Food("E", 20, 30, 33, 44, 55, 66, timee5),
            Food("F", 20, 30, 33, 44, 55, 66, timee6),
            Food("G", 20, 30, 33, 44, 55, 66, timee7),
            Food("H", 20, 30, 33, 44, 55, 66, timee8),
            Food("I", 20, 30, 33, 44, 55, 66, timee9),
            Food("J", 20, 30, 33, 44, 55, 66, timee10),
            Food("K", 20, 30, 33, 44, 55, 66, timee11),
        )

        dbRef.child(year).child(month).child("27").child(testList2[0].foodName)
            .setValue(testList2[0])
        dbRef.child(year).child(month).child("27").child(testList2[1].foodName)
            .setValue(testList2[1])
        dbRef.child(year).child(month).child("26").child(testList2[2].foodName)
            .setValue(testList2[2])
        dbRef.child(year).child(month).child("26").child(testList2[3].foodName)
            .setValue(testList2[3])
        dbRef.child(year).child(month).child("26").child(testList2[4].foodName)
            .setValue(testList2[4])
        dbRef.child(year).child(month).child("26").child(testList2[5].foodName)
            .setValue(testList2[5])
        dbRef.child(year).child(month).child("26").child(testList2[6].foodName)
            .setValue(testList2[6])
        dbRef.child(year).child(month).child("26").child(testList2[7].foodName)
            .setValue(testList2[7])
        dbRef.child(year).child(month).child("26").child(testList2[5].foodName)
            .setValue(testList2[8])
        dbRef.child(year).child(month).child("26").child(testList2[6].foodName)
            .setValue(testList2[9])
        dbRef.child(year).child(month).child("26").child(testList2[7].foodName)
            .setValue(testList2[10])
        showFoodListToday(true)


        // Inflate the layout for this fragment
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
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

                    R.id.TakePhotoButton -> {
                        removeAllButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()//begins screen transition
                        lifecycleScope.launch {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_mainFragment_to_cameraFragment)
                        }
                    }

                    R.id.WriteFoodButton -> {
                        removeAllButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()//begins screen transition
                        lifecycleScope.launch {
                            delay(Constants.transitionStartTime)
                            val navigateToWriteFragWithNoFood =
                                MainFragmentDirections.actionMainFragmentToWriteFragment(Food())
                            binding.root.findNavController().navigate(navigateToWriteFragWithNoFood)
                        }
                    }

                    R.id.homeScreenButton -> {
                        removeAllButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()//begins screen transition
                        lifecycleScope.launch {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_mainFragment_to_homeFragment)
                        }

                    }

                    R.id.main_ButtonMorning -> {
                        if (previouslyClickedIcon != binding.mainButtonMorning) {
                            previouslyClickedIcon = binding.mainButtonMorning
                            IconClickResponse(R.id.main_ButtonMorning)
                        }
                    }

                    R.id.main_ButtonAfternoon -> {
                        if (previouslyClickedIcon != binding.mainButtonAfternoon) {
                            previouslyClickedIcon = binding.mainButtonAfternoon
                            IconClickResponse(R.id.main_ButtonAfternoon)
                        }
                    }

                    R.id.main_ButtonNight -> {
                        if (previouslyClickedIcon != binding.mainButtonNight) {
                            previouslyClickedIcon = binding.mainButtonNight
                            IconClickResponse(R.id.main_ButtonNight)
                        }
                    }

                    R.id.main_ButtonAll -> {
                        if (previouslyClickedIcon != binding.mainButtonAll) {
                            previouslyClickedIcon = binding.mainButtonAll
                            IconClickResponse(R.id.main_ButtonAll)
                        }
                    }


                }
            }
        binding.NewFoodButton.setOnClickListener(buttonsClickListener)
        binding.TakePhotoButton.setOnClickListener(buttonsClickListener)
        binding.WriteFoodButton.setOnClickListener(buttonsClickListener)
        binding.homeScreenButton.setOnClickListener(buttonsClickListener)
        binding.mainButtonMorning.setOnClickListener(buttonsClickListener)
        binding.mainButtonAfternoon.setOnClickListener(buttonsClickListener)
        binding.mainButtonNight.setOnClickListener(buttonsClickListener)
        binding.mainButtonAll.setOnClickListener(buttonsClickListener)


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

        return binding.root
    }

    fun logFoodGUI() {
        isUp = !isUp
        if (isUp) {
            binding.TakePhotoButton.show()
            binding.WriteFoodButton.show()
        } else {
            binding.TakePhotoButton.hide()
            binding.WriteFoodButton.hide()
        }
    }

    fun refreshCheckerLoop() { //Checks if the screen needs to be refreshed for new foods
        lifecycleScope.launch(Dispatchers.IO) {
            while (true) {
                delay(500L)
                if (refreshScreen) {
                    showFoodListToday(false)
                    refreshScreen = false
                    break
                }
            }
            this.cancel()//TODO may cause problems
        }
    }

    fun showFoodListToday(isStartUp: Boolean) {
        foodList = mutableListOf<Food>()
        dbRef.get().addOnSuccessListener { snapshot ->
            var foodSnapShot = snapshot.child(year).child(month).child(day).children
            for (foodItem in foodSnapShot) {
                val name = foodItem.child("foodName").value.toString()
                val calorie = foodItem.child("calories").value.toString().toInt()
                val fat = foodItem.child("fat").value.toString().toInt()
                val sugar = foodItem.child("sugar").value.toString().toInt()
                val sodium = foodItem.child("sodium").value.toString().toInt()
                val protein = foodItem.child("protein").value.toString().toInt()
                val carbohydrate = foodItem.child("carbohydrate").value.toString().toInt()
                val imageUriString = foodItem.child("imageUriString").value.toString()
                val time = convertToTime(foodItem.child("timeLogged"))
                val newFood = Food(
                    name ?: "", calorie ?: 0, fat ?: 0, sugar ?: 0,
                    sodium ?: 0, protein ?: 0, carbohydrate ?: 0, time, imageUriString
                )
                foodList.add(newFood)
            }

            for (i in 0 until foodList.size - 1) {
                for (j in 0 until foodList.size - 1 - i) {
                    if (foodList[j].timeLogged.compareTo(foodList[j + 1].timeLogged) > 0) {
                        val foodAtIndex = foodList[j]
                        foodList[j] = foodList[j + 1]
                        foodList[j + 1] = foodAtIndex
                    }
                }
            }

            adapter = FoodAdapter(foodList, this)
            binding.amountLoggedTV.text = "You have logged ${foodList.size} items"
            foodListSize = foodList.size

            binding.recyclerView.recycledViewPool.setMaxRecycledViews(
                0,
                0
            ) //prevents bug where some items may disappear by setting the view to be invisible
            binding.recyclerView.adapter = adapter
            if (isStartUp) {
                (activity as MainActivity?)!!.endTransition()//starts the ending transition upon onCreateView
            }
            //TODO May have to add condition to determine if transition plays
        }
    }

    fun updateFoodListValues(State: Int) {
        foodList = mutableListOf<Food>()
        dbRef.get().addOnSuccessListener { snapshot ->
            var foodSnapShot = snapshot.child(year).child(month).child(day).children
            for (foodItem in foodSnapShot) {
                val name = foodItem.child("foodName").value.toString()
                val calorie = foodItem.child("calories").value.toString().toInt()
                val fat = foodItem.child("fat").value.toString().toInt()
                val sugar = foodItem.child("sugar").value.toString().toInt()
                val sodium = foodItem.child("sodium").value.toString().toInt()
                val protein = foodItem.child("protein").value.toString().toInt()
                val carbohydrate = foodItem.child("carbohydrate").value.toString().toInt()
                val imageUriString = foodItem.child("imageUriString").value.toString()
                val time = convertToTime(foodItem.child("timeLogged"))
                val newFood = Food(
                    name ?: "", calorie ?: 0, fat ?: 0, sugar ?: 0,
                    sodium ?: 0, protein ?: 0, carbohydrate ?: 0, time, imageUriString
                )
                foodList.add(newFood)
            }

            for (i in 0 until foodList.size - 1) {
                for (j in 0 until foodList.size - 1 - i) {
                    if (foodList[j].timeLogged.compareTo(foodList[j + 1].timeLogged) > 0) {
                        val foodAtIndex = foodList[j]
                        foodList[j] = foodList[j + 1]
                        foodList[j + 1] = foodAtIndex
                    }
                }
            }

//            adapter = FoodAdapter(foodList, this)
//            binding.amountLoggedTV.text = "You have logged ${foodList.size} items"
            foodListSize = foodList.size
            if (State == 1) {
                showMorningList()
            } else if (State == 2) {
                showAfternoonList()
            } else if (State == 3) {

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.cancel()
        _binding = null
    }

    fun printfood() {
        Log.d("MainActivity", "possible")
    }


    fun convertToTime(foodItem: DataSnapshot): LocalDateTime {
        val hour = (foodItem.child("hour").value as Long).toInt()
        val monthValue = (foodItem.child("monthValue").value as Long).toInt()
        val day = (foodItem.child("dayOfMonth").value as Long).toInt()
        val year = (foodItem.child("year").value as Long).toInt()
        val minute = (foodItem.child("minute").value as Long).toInt()
        val second = (foodItem.child("second").value as Long).toInt()
        return LocalDateTime.of(year, monthValue, day, hour, minute, second)
    }

    fun IconClickResponse(Id: Int) {
        binding.iconClickAnimation.updateLayoutParams<ConstraintLayout.LayoutParams> {
            startToStart = Id
            bottomToBottom = Id
            topToTop = Id
            endToEnd = Id
        }
        binding.iconClickAnimation.playAnimation()

        if (Id == R.id.main_ButtonMorning) {
            updateFoodListValues(1)
        } else if (Id == R.id.main_ButtonAfternoon) {
            updateFoodListValues(2)
        } else if (Id == R.id.main_ButtonNight) {
            updateFoodListValues(3)
        } else if (Id == R.id.main_ButtonAll) {
            updateFoodListValues(4)
        } else {
            Log.d("MainFragment", "IconClickResponse no matching id")
        }
    }

    fun showMorningList() {
        Log.d("MainFragment", "${foodList}")
        var morningFoodList = mutableListOf<Food>()
        for (i in 0 until foodList.size) {
            Log.d(
                "MainFragment",
                "${foodList[i].timeLogged.hour} AND ${Constants.morningStartHour} AND ${Constants.afternoonStartHour}"
            )

            if (foodList[i].timeLogged.hour >= Constants.morningStartHour && foodList[i].timeLogged.hour < Constants.afternoonStartHour) {
                morningFoodList.add(foodList[i])
            }
        }
        adapter = FoodAdapter(morningFoodList, this)
        Log.d("MainFragment", "${morningFoodList}")
        binding.recyclerView.recycledViewPool.setMaxRecycledViews(
            0,
            0
        ) //prevents bug where some items may disappear by setting the view to be invisible
        binding.recyclerView.adapter = adapter
    }

    fun showAfternoonList() {

    }

    fun removeAllButtonFunctionality() {
        binding.homeScreenButton.isClickable = false
        binding.WriteFoodButton.isClickable = false
        binding.TakePhotoButton.isClickable = false
    }


    companion object {
        lateinit var dbRef: DatabaseReference
        var systemTime = LocalDateTime.now()
        var year = systemTime.year.toString()
        var month = systemTime.month.toString()
        var day = systemTime.dayOfMonth.toString()
        var refreshScreen = false

        fun removeItemInList(name: String) {
            Log.d("MainActivity", "$name")
            dbRef.child(year).child(month).child(day).child(name).ref.removeValue()


        }


    }
}