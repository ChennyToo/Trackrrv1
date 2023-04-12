package com.example.googlebooksnetworking

import com.squareup.moshi.Moshi
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Retrofit
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://www.googleapis.com/books/v1/"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(BASE_URL).build()
private const val QUERY_STRING =
    "volumes?q=Android" +
            "&maxResults=10"


interface BookApiService {
    @GET(QUERY_STRING)
    fun getBooks(): Call<BooksResponse>
}

object BookApi {
    val BookApi : BookApiService by lazy{
        retrofit.create(BookApiService::class.java)
    }

}
