package com.example.trackrrv1

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodViewModel : ViewModel() {
    private val _response = MutableLiveData<List<Food>>()
        val response: LiveData<List<Food>>
            get() = _response

//    var ViewModelUPC: Long = 0
//
//    fun storeUPC(UPC : Long){
//        ViewModelUPC = UPC
//    }
    fun getFoods(code : Long){
        val request = FoodApi.FoodApi.getFoods(code, "3018c32b", "cb2bb40afcee0aaeb8e01060a5abf237")
    request.enqueue(object : Callback<FoodsResponse> {
            override fun onFailure(call: Call<FoodsResponse>, t: Throwable) {
                Log.d("RESPONSE", "Failure: " + t.message)
            }
            override fun onResponse(call: Call<FoodsResponse>, response: Response<FoodsResponse>) {
                var listOfFoodsFetched = mutableListOf<Food>()
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
                    val imageUri = foodItem.imageLinks.thumbnail!!.toUri().buildUpon().scheme("https").build()

                    val newFood = Food(name?: "", calorie?: 0, fat?: 0, sugar?: 0,
                        sodium?: 0, protein?: 0, carbohydrate?: 0,imageUri)
                    listOfFoodsFetched.add(newFood)
                    Log.d("Info", "$name $calorie $fat $sugar $imageUri")
                }
                _response.value = listOfFoodsFetched
            }
        })
    }

}