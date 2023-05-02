package com.example.trackrrv1

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class FoodViewModel : ViewModel() {
    private val _response = MutableLiveData<MutableList<Food>>()
        val response: LiveData<MutableList<Food>>
            get() = _response
    var listOfFoodsFetched = mutableListOf<Food>()
    var dbRef = Firebase.database.reference
    var systemTime = LocalDateTime.now()
    var year = systemTime.year.toString()
    var month = systemTime.month.toString()
    val _day = MutableLiveData<String>()
    val day: LiveData<String>
        get() = _day

    var currentFoodNumber = 0





    fun getFoods(code : Long){
        val request = FoodApi.FoodApi.getFoods(code, "3018c32b", "cb2bb40afcee0aaeb8e01060a5abf237")
    request.enqueue(object : Callback<FoodsResponse> {
            override fun onFailure(call: Call<FoodsResponse>, t: Throwable) {
                Log.d("RESPONSE", "Failure: " + t.message)
            }
            override fun onResponse(call: Call<FoodsResponse>, response: Response<FoodsResponse>) {
                val foodsResponse : FoodsResponse? = response.body()
                val foodsItemList = foodsResponse?.foodsItemsList ?: listOf()

                for (foodItem in foodsItemList){
                    val name = foodItem.name
                    val calorie = foodItem.calorie
                    val fat = foodItem.fat
                    val sugar = foodItem.sugar
                    val sodium = foodItem.sodium
                    val protein = foodItem.protein
                    val carbohydrate = foodItem.carbohydrate
                    val imageUriString = foodItem.imageLinks.thumbnail!!.toUri().buildUpon().scheme("https").build().toString()

                    val newFood = Food(name?: "", calorie?: 0, fat?: 0, sugar?: 0,
                        sodium?: 0, protein?: 0, carbohydrate?: 0, LocalDateTime.now(), imageUriString)
//                    listOfFoodsFetched.add(newFood)
                    dbRef = Firebase.database.reference
                    dbRef.child(year).child(month).child(day.value!!).child(name!!).setValue(newFood).addOnSuccessListener {
                    }
                    currentFoodNumber++
                }
//                _response.value = listOfFoodsFetched
            }


        })
    }

}