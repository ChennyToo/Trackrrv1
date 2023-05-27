package com.example.trackrrv1

import android.net.Uri
import android.os.Parcelable
import androidx.core.net.toUri
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
@Parcelize
data class Food (val foodName : String = "empty",
                 val calories : Int = 0,
                 val fat : Int = 0,
                 val sugar: Int = 0,
                 val sodium : Int = 0,
                 val protein: Int = 0,
                 val carbohydrate: Int = 0,
                 val timeLogged: LocalDateTime = LocalDateTime.now(),
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

