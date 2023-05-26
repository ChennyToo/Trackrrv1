package com.example.trackrrv1

import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toUri
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
@Parcelize
data class Food (val foodName : String,
                 val calories : Int,
                 val fat : Int,
                 val sugar: Int,
                 val sodium : Int,
                 val protein: Int,
                 val carbohydrate: Int,
                 val timeLogged: LocalDateTime,
                 val imageUriString: String = "https://nutritionix-api.s3.amazonaws.com/5460f139a7f2fb4538e920ae.jpeg"
                 ) : Parcelable {
    @Exclude

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "foodName" to foodName,
            "calories" to calories,
            "fat" to fat,
            "sugar" to sugar,
            "sodium" to sodium,
            "protein" to protein,
            "carbohydrate" to carbohydrate,
            "timeLogged" to timeLogged,
            "imageUriString" to imageUriString,
        )
    }
}

