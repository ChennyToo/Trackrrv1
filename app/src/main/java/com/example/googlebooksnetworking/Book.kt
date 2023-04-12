package com.example.googlebooksnetworking

import android.net.Uri

data class Book (val title : String, val subtitle : String, val author : List<String>, val url :String, val imageUri: Uri)