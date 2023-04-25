package com.example.trackrrv1

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://trackapi.nutritionix.com/v2/"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit =
        Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL).build()
    var apiUPCCode: Long = 0
        get() = CameraFragment.UPC
    private var QUERY_STRING =
        "search/item?upc=${apiUPCCode}" +
                "&x-app-id=3018c32b" +
                "&x-app-key=cb2bb40afcee0aaeb8e01060a5abf237"

    private var AUTH_STRING = "&x-app-id=3018c32b" +
            "&x-app-key=cb2bb40afcee0aaeb8e01060a5abf237"

    interface FoodApiService {
        @GET("search/item")
        fun getFoods(@Query("upc") code : Long, @Query("x-app-id") id : String, @Query("x-app-key") key : String): Call<FoodsResponse>

    }

    object FoodApi {
        val FoodApi: FoodApiService by lazy {
            retrofit.create(FoodApiService::class.java)
        }
    }

