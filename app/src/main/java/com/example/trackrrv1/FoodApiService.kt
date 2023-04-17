package com.example.trackrrv1

import com.squareup.moshi.Moshi
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
private const val BASE_URL = "https://trackapi.nutritionix.com/v2/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()
private const val QUERY_STRING =
    "search/item?upc=034000121465&x-app-id=3018c32b&x-app-key=cb2bb40afcee0aaeb8e01060a5abf237"


interface FoodApiService {
    @GET(QUERY_STRING)
    fun getFoods(): Call<FoodsResponse>
}

object FoodApi {
    val FoodApi : FoodApiService by lazy{
        retrofit.create(FoodApiService::class.java)
    }
}
