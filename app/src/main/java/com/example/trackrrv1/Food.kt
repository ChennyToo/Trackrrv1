package com.example.trackrrv1

import android.net.Uri

data class Food (val foodName : String,
                 val calories : Int,
                 val fat : Int,
                 val sugar: Int,
                 val sodium : Int,
                 val protein: Int,
                 val carbohydrate: Int,
                 val imageUri: Uri)