package com.example.googlebooksnetworking

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookViewModel : ViewModel() {
    private val _response = MutableLiveData<List<Book>>()
        val response: LiveData<List<Book>>
            get() = _response


    fun getBooks(){
        val request = BookApi.BookApi.getBooks()
        request.enqueue(object : Callback<BooksResponse> {
            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                Log.d("RESPONSE", "Failure: " + t.message)
            }
            override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                var listOfBooksFetched = mutableListOf<Book>()
                val booksResponse : BooksResponse? = response.body()
                val booksItemList = booksResponse?.booksItemsList ?: listOf()

                for (bookItems in booksItemList){
                    val booksVolumeInfo = bookItems.booksVolumeInfo
                    val title = booksVolumeInfo.title
                    val authors = booksVolumeInfo.authors
                    val subtitle = booksVolumeInfo.subtitle
                    val url = booksVolumeInfo.url
                    val imageUri = booksVolumeInfo.imageLinks.image!!.toUri().buildUpon().scheme("https").build()

                    val newBook = Book(title?: "", subtitle?: "", authors?: listOf<String>(), url?: "", imageUri)
                    listOfBooksFetched.add(newBook)
                    Log.d("Info", "${booksVolumeInfo.imageLinks.image}")
                }
                _response.value = listOfBooksFetched
            }
        })
    }

}