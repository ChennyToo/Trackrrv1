package com.example.trackrrv1

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class FoodViewModel : ViewModel() {
    private val _response = MutableLiveData<MutableList<Food>>()
        val response: LiveData<MutableList<Food>>
            get() = _response
    var dbRef = Constants.userDatabaseReference
    val systemTime //Time variables are getters because time always changes
        get() = LocalDateTime.now()
    val year
        get() = systemTime.year.toString()
    val month
        get() = systemTime.month.toString()
    val day
        get() = systemTime.dayOfMonth.toString()


    var todayCalorie = 0
    val field1value = MutableLiveData(-1)
    val field2value = MutableLiveData(-1)
    val field3value = MutableLiveData(-1)
    var foodFromCamera = MutableLiveData(Food("initializedFoodFromCamera"))//Mutable live data because CameraFragment needs to know when the information updates in order to navigate


    fun getNutritionToday(nutrient : String){
        dbRef.get().addOnSuccessListener { snapshot ->
            var foodSnapShot = snapshot.child(year).child(month).child(
                MainFragment.day
            ).children
            var value = 0
            for (foodItem in foodSnapShot) {
                value += foodItem.child(nutrient).value.toString().toInt()
            }

            if (Constants.HCField1Type.equals(nutrient)){
                field1value.value = value
            }

            else if (Constants.HCField2Type.equals(nutrient)){
                field2value.value = value
            }

            else if (Constants.HCField3Type.equals(nutrient)){
                field3value.value = value
            }

            else {
                Log.d("MainActivity", "Nutrition Field Error, no field matches nutrient")
            }

        }
    }


    fun getCalorieToday(){//Obtains total amount of calories eaten today from Firebase
        dbRef.get().addOnSuccessListener { snapshot ->
            var foodSnapShot = snapshot.child(year).child(month).child(
                MainFragment.day
            ).children
            var cal = 0
            for (foodItem in foodSnapShot) {
                val calorie = foodItem.child("calories").value.toString().toInt()
                cal += calorie
            }
            todayCalorie = cal
            HomeFragment.checkCalories = true //Tells the HomeFragment to start the calorie progress bar animation
        }
    }

    fun getFoods(code : Long){//Barcode scanner gets the number ID and requests to API
        val request = FoodApi.FoodApi.getFoods(code, "3018c32b", "cb2bb40afcee0aaeb8e01060a5abf237")
    request.enqueue(object : Callback<FoodsResponse> {
            override fun onFailure(call: Call<FoodsResponse>, t: Throwable) {
                Log.d("RESPONSE", "Failure: " + t.message)
                //erroneous barcode does not result in failure
            }
            //DO NOTE THAT APP IS LIMITED TO 50 API CALLS PER DAY AND WILL RETURN EMPTY LIST IF SO
            override fun onResponse(call: Call<FoodsResponse>, response: Response<FoodsResponse>) {
                val foodsResponse : FoodsResponse? = response.body()
                Log.d("FoodViewModel", "${foodsResponse}")
                val foodsItemList = foodsResponse?.foodsItemsList ?: listOf()
                Log.d("FoodViewModel", "${foodsItemList}")

                for (foodItem in foodsItemList){
                    Log.d("FoodViewModel", "Called1")
                    val name = if(foodItem.name!!.length > 16){foodItem.name!!.substring(0, 16)}else{foodItem.name}
                    val calorie = foodItem.calorie
                    val fat = foodItem.fat
                    val sugar = foodItem.sugar
                    val sodium = foodItem.sodium
                    val protein = foodItem.protein
                    val carbohydrate = foodItem.carbohydrate
                    val imageUriString = foodItem.imageLinks.thumbnail!!.toUri().buildUpon().scheme("https").build().toString()
                    //Have the imageUriString be uploaded to Storage

                    val newFood = Food(name?: "empty", calorie?: 0, fat?: 0, sugar?: 0,
                        sodium?: 0, protein?: 0, carbohydrate?: 0, LocalDateTime.now(), imageUriString)
                    foodFromCamera.value = newFood

                }

                if(foodsItemList.isEmpty()){
                    Log.d("FoodViewModel", "Called2")
                    foodFromCamera.value = Food()
                }

            }


        })
    }

}