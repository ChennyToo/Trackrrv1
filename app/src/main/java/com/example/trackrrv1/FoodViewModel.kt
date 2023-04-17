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

    fun getBooks(){
        val request = FoodApi.FoodApi.getFoods()
        request.enqueue(object : Callback<FoodsResponse> {
            override fun onFailure(call: Call<FoodsResponse>, t: Throwable) {
                Log.d("RESPONSE", "Failure: " + t.message)
            }
            override fun onResponse(call: Call<FoodsResponse>, response: Response<FoodsResponse>) {
                var listOfFoodsFetched = mutableListOf<Food>()
                val foodsResponse : FoodsResponse? = response.body()
//                val booksItemList = booksResponse?.booksItemsList ?: listOf()
//
//                for (bookItems in booksItemList){
//                    val booksVolumeInfo = bookItems.booksVolumeInfo
//                    val title = booksVolumeInfo.title
//                    val authors = booksVolumeInfo.authors
//                    val subtitle = booksVolumeInfo.subtitle
//                    val url = booksVolumeInfo.url
//                    val imageUri = booksVolumeInfo.imageLinks.image!!.toUri().buildUpon().scheme("https").build()
//
//                    val newFood = Food(title?: "", subtitle?: "", authors?: listOf<String>(), url?: "", imageUri)
//                    listOfBooksFetched.add(newFood)
//                    Log.d("Info", "${booksVolumeInfo.imageLinks.image}")
//                }
                _response.value = listOfFoodsFetched
            }
        })
    }

}