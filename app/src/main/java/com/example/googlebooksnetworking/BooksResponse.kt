package com.example.googlebooksnetworking

import com.squareup.moshi.Json

class BooksResponse {
    @Json(name = "items")
    lateinit var booksItemsList : List<BookItems>
}

class BookItems {
    @Json(name = "volumeInfo")
    lateinit var booksVolumeInfo : BooksVolumeInfo
}

class BooksVolumeInfo{
    @Json(name = "title")
    var title :String? = ""
    @Json(name = "authors")
    var authors :List<String>? = listOf<String>()
    @Json(name = "subtitle")
    var subtitle :String? = ""
    @Json(name = "infoLink")
    var url : String? =""
    @Json(name = "imageLinks")
    lateinit var imageLinks : Thumbnail
}

class Thumbnail{
    @Json(name = "thumbnail")
    var image : String? = ""
}