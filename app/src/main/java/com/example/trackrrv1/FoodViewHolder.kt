package com.example.trackrrv1

import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackrrv1.databinding.ListItemLayoutBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FoodViewHolder(val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentFood: Food


    fun bindFood(food: Food) {
        currentFood = food

        val name = currentFood.foodName
        val calories = currentFood.calories
        val fat = currentFood.fat
        val sugar = currentFood.sugar
        val protein = currentFood.protein
        val carb = currentFood.carbohydrate
        val time = currentFood.timeLogged
        var hour = time.hour
        val isAM = if (hour < 12) {
            "AM"
        } else {
            "PM"
        } //Format the time logic
        if (isAM == "PM") {//Subtract 12 because PM is 12 hours ahead of AM
            hour -= 12
        }
        if (hour == 0) {//No such thing as 00:12 PM, it is written as 12:12 PM
            hour = 12
        }
        val hourString = if (hour.toString().length == 1) {
            "0$hour"
        } else {
            hour.toString()
        }//Includes the zero placeholder if one digit
        val minuteString = if (time.minute.toString().length == 1) {
            "0${time.minute}"
        } else {
            time.minute
        }
        val timeString = "$hourString:$minuteString \n$isAM"
        val foodTimeIdentifier = "${time.hour}${time.minute}"
        val foodStorageID = "${Constants.username}/${currentFood.foodName}${foodTimeIdentifier}"

//TODO CHANGE THE NAMES
        binding.CalorieTextView.text = "$calories"
        binding.nutrientTV3.text = "Fat: ${fat}g"
        binding.NameTextView.text = name
        binding.nutrientTV1.text = "Protein: ${protein}g"
        binding.nutrientTV2.text = "Carbs: ${carb}g"
        binding.nutrientTV4.text = "Sugar: ${sugar}g"
        binding.timeLoggedTV.text = timeString

        if (currentFood.imageUriString != "") {
            Log.d("FoodViewHolder", "${currentFood.imageUriString}")
            Glide.with(itemView).load(currentFood.imageUriString.toUri())
                .into(binding.FoodImageView);
        }
        else {
            bindImageFromFirebase(foodStorageID, 500L)
        }
    }

    fun bindImageFromFirebase(id: String, delayIteration: Long) {
        //Below code gets the image from Firebase Storage and loads it into the respective food item
        MainFragment.mStorageRef.child(id).downloadUrl.addOnSuccessListener {
            Glide.with(parentFragment!!)
                .load(it)
                .into(binding.FoodImageView)
            Log.d("FoodViewHolder", "download passed")
        }
            .addOnFailureListener {//Recursive code that will constantly try and attempt to get the image from Storage even after failing to get it the first time
                Log.d("FoodViewHolder", "download failed, retrying")
                parentFragment!!.lifecycleScope.launch {
                    delay(delayIteration)
                    bindImageFromFirebase(id, delayIteration + 500L)
                }
            }
    }

    companion object {
        var parentFragment: Fragment? = null
    }


}