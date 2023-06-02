package com.example.trackrrv1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    private val viewModel: FoodViewModel by activityViewModels()
    var isUp = false
    var firstClick = true
    lateinit var foodList: MutableList<Food>
    lateinit var adapter: FoodAdapter

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
        FoodViewHolder.parentFragment = this
        systemTime = viewModel.systemTime
        year = viewModel.year
        month = viewModel.month
        day = viewModel.day
        isUp = false
        firstClick = true
        binding.TakePhotoButton.isClickable = false
        binding.WriteFoodButton.isClickable = false
        setTopTimeOfDay(LocalDateTime.now().hour)
        var previouslyClickedIcon = binding.mainButtonAll
        dbRef = Constants.userDatabaseReference



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
        var timee11 = LocalDateTime.of(2023, 5, 27, 23, 30)

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

//        dbRef.child(year).child(month).child(day).child(testList2[0].foodName)
//            .setValue(testList2[0])
//        dbRef.child(year).child(month).child(day).child(testList2[1].foodName)
//            .setValue(testList2[1])
//        dbRef.child(year).child(month).child(day).child(testList2[2].foodName)
//            .setValue(testList2[2])
//        dbRef.child(year).child(month).child(day).child(testList2[3].foodName)
//            .setValue(testList2[3])
//        dbRef.child(year).child(month).child(day).child(testList2[4].foodName)
//            .setValue(testList2[4])
//        dbRef.child(year).child(month).child(day).child(testList2[5].foodName)
//            .setValue(testList2[5])
//        dbRef.child(year).child(month).child(day).child(testList2[6].foodName)
//            .setValue(testList2[6])
//        dbRef.child(year).child(month).child(day).child(testList2[7].foodName)
//            .setValue(testList2[7])
//        dbRef.child(year).child(month).child(day).child(testList2[8].foodName)
//            .setValue(testList2[8])
//        dbRef.child(year).child(month).child(day).child(testList2[9].foodName)
//            .setValue(testList2[9])
//        dbRef.child(year).child(month).child(day).child(testList2[10].foodName)
//            .setValue(testList2[10])
        showFoodListToday()



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
                        removeNavButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()//begins screen transition
                        lifecycleScope.launch {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_mainFragment_to_cameraFragment)
                        }
                    }

                    R.id.WriteFoodButton -> {
                        removeNavButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()//begins screen transition
                        lifecycleScope.launch {
                            delay(Constants.transitionStartTime)
                            val navigateToWriteFragWithNoFood =
                                MainFragmentDirections.actionMainFragmentToWriteFragment(Food())
                            binding.root.findNavController().navigate(navigateToWriteFragWithNoFood)
                        }
                    }

                    R.id.homeScreenButton -> {
                        removeNavButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()//begins screen transition
                        lifecycleScope.launch {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_mainFragment_to_homeFragment)
                        }

                    }

                    R.id.main_ButtonMorning -> {
                        if (previouslyClickedIcon != binding.mainButtonMorning) {
                            setIconClickCooldown()
                            previouslyClickedIcon = binding.mainButtonMorning
                            IconClickResponse(R.id.main_ButtonMorning)
                        }
                    }

                    R.id.main_ButtonAfternoon -> {
                        if (previouslyClickedIcon != binding.mainButtonAfternoon) {
                            setIconClickCooldown()
                            previouslyClickedIcon = binding.mainButtonAfternoon
                            IconClickResponse(R.id.main_ButtonAfternoon)
                        }
                    }

                    R.id.main_ButtonNight -> {
                        if (previouslyClickedIcon != binding.mainButtonNight) {
                            setIconClickCooldown()
                            previouslyClickedIcon = binding.mainButtonNight
                            IconClickResponse(R.id.main_ButtonNight)
                        }
                    }

                    R.id.main_ButtonAll -> {
                        if (previouslyClickedIcon != binding.mainButtonAll) {
                            setIconClickCooldown()
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


        return binding.root
    }

    fun logFoodGUI() {
        isUp = !isUp
        if (isUp) {
            binding.TakePhotoButton.isClickable = true
            binding.WriteFoodButton.isClickable = true
        } else {
            binding.TakePhotoButton.isClickable = false
            binding.WriteFoodButton.isClickable = false
        }
    }



    fun showFoodListToday() {
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
            binding.recyclerView.recycledViewPool.setMaxRecycledViews(
                0,
                0
            ) //prevents bug where some items may disappear by setting the view to be invisible
                 binding.recyclerView.adapter = adapter
                (activity as MainActivity?)!!.endTransition(Constants.transitionEndTime)//starts the ending transition upon onCreateView
                binding.recyclerView.layoutManager?.scrollToPosition(foodList.size - 1)
            setIfNoImageIcon(foodList.size)
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
            if (State == 1) {
                showMorningList()
            } else if (State == 2) {
                showAfternoonList()
            } else if (State == 3) {
                showNightList()
            } else if (State == 4) {
                showAllList()
            } else {
                Log.d("MainFragment", "updateFoodListValues State Mismatch")
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
        var morningFoodList = mutableListOf<Food>()
        for (i in 0 until foodList.size) {
            if (foodList[i].timeLogged.hour >= Constants.morningStartHour && foodList[i].timeLogged.hour < Constants.afternoonStartHour) {
                morningFoodList.add(foodList[i])
            }
        }
        setIfNoImageIcon(morningFoodList.size)
        adapter = FoodAdapter(morningFoodList, this)
        binding.recyclerView.recycledViewPool.setMaxRecycledViews(
            0,
            0
        ) //prevents bug where some items may disappear by setting the view to be invisible
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager?.scrollToPosition(morningFoodList.size - 1)
    }

    fun showAfternoonList() {
        var afternoonFoodList = mutableListOf<Food>()
        for (i in 0 until foodList.size) {
            if (foodList[i].timeLogged.hour >= Constants.afternoonStartHour && foodList[i].timeLogged.hour < Constants.nightStartHour) {
                afternoonFoodList.add(foodList[i])
            }
        }
        setIfNoImageIcon(afternoonFoodList.size)
        adapter = FoodAdapter(afternoonFoodList, this)
        binding.recyclerView.recycledViewPool.setMaxRecycledViews(
            0,
            0
        ) //prevents bug where some items may disappear by setting the view to be invisible
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager?.scrollToPosition(afternoonFoodList.size - 1)


    }


fun showNightList() {
    var nightFoodList = mutableListOf<Food>()
    for (i in 0 until foodList.size) {
        if (foodList[i].timeLogged.hour >= Constants.nightStartHour || foodList[i].timeLogged.hour < Constants.morningStartHour) {
            nightFoodList.add(foodList[i])
        }
    }
    setIfNoImageIcon(nightFoodList.size)
    adapter = FoodAdapter(nightFoodList, this)
    binding.recyclerView.recycledViewPool.setMaxRecycledViews(
        0,
        0
    ) //prevents bug where some items may disappear by setting the view to be invisible
    binding.recyclerView.adapter = adapter
    binding.recyclerView.layoutManager?.scrollToPosition(nightFoodList.size - 1)
}

fun showAllList() {
    setIfNoImageIcon(foodList.size)
    adapter = FoodAdapter(foodList, this)
    binding.recyclerView.recycledViewPool.setMaxRecycledViews(
        0,
        0
    ) //prevents bug where some items may disappear by setting the view to be invisible
    binding.recyclerView.adapter = adapter
    binding.recyclerView.layoutManager?.scrollToPosition(foodList.size - 1)
}

private fun removeNavButtonFunctionality() {
    binding.homeScreenButton.isClickable = false
    binding.WriteFoodButton.isClickable = false
    binding.TakePhotoButton.isClickable = false
}

    fun setIconClickCooldown(){
        lifecycleScope.launch(Dispatchers.Main) {
            binding.mainButtonMorning.isClickable = false
            binding.mainButtonAfternoon.isClickable = false
            binding.mainButtonNight.isClickable = false
            binding.mainButtonAll.isClickable = false
            delay(Constants.main_iconCooldownTime)
            binding.mainButtonMorning.isClickable = true
            binding.mainButtonAfternoon.isClickable = true
            binding.mainButtonNight.isClickable = true
            binding.mainButtonAll.isClickable = true

        }
    }

    fun setTopTimeOfDay(hour : Int){
        if (hour >= Constants.nightStartHour || hour < Constants.morningStartHour){
            binding.mainTop.setBackgroundResource(R.drawable.main_topthree)
        }

        else if (hour >= Constants.afternoonStartHour){
            binding.mainTop.setBackgroundResource(R.drawable.main_toptwo)
        }

        else {
            binding.mainTop.setBackgroundResource(R.drawable.main_topone)
        }
    }

    fun setIfNoImageIcon(sizeOfList: Int){
        if (sizeOfList < 1){
            binding.mainNoImageIcon.visibility = View.VISIBLE
            binding.mainNoFoodText.visibility = View.VISIBLE
        }

        else {
            binding.mainNoImageIcon.visibility = View.INVISIBLE
            binding.mainNoFoodText.visibility = View.INVISIBLE
        }
    }


companion object {
    var dbRef: DatabaseReference = Constants.userDatabaseReference
    var systemTime = LocalDateTime.now()
    var year = systemTime.year.toString()
    var month = systemTime.month.toString()
    var day = systemTime.dayOfMonth.toString()
    var mStorageRef: StorageReference =  FirebaseStorage.getInstance().getReference("uploads")

    fun removeItemInList(name: String) {
        Log.d("MainActivity", "$name")
        dbRef.child(year).child(month).child(day).child(name).ref.removeValue()
        //TODO Remove image from Storage as well
    }


}
}