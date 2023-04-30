package com.example.trackrrv1

import android.net.Uri
import androidx.core.net.toUri
import java.time.LocalDateTime

data class Food (val foodName : String,
                 val calories : Int,
                 val fat : Int,
                 val sugar: Int,
                 val sodium : Int,
                 val protein: Int,
                 val carbohydrate: Int,
                 val timeLogged: LocalDateTime,
                 val imageUriString: String = "https://nutritionix-api.s3.amazonaws.com/5460f139a7f2fb4538e920ae.jpeg"
                 )

