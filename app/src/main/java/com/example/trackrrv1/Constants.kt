package com.example.trackrrv1

import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Constants {
    companion object{
        //TODO Implement Shared preferences so settings save
        var username : String = "empty"
        lateinit var userDatabaseReference : DatabaseReference
        var transitionStartTime : Long = 400L//Upon finishing starttime, that is when navigation should occur
        var transitionEndTime : Long = 400L
        var transitionEndTimeQuick : Long = 100L
        var main_customizeAnimationTime : Long = 800L
        var main_iconCooldownTime : Long = 300L
        var calorieIntake : Int = 2000
        var proteinIntake : Int = 50//update based on user preference
        var carbIntake : Int = 300
        var fatIntake : Int = 60
        var sodiumIntake : Int = 2000
        var sugarIntake : Int = 30
        var monthList : List<String> = listOf("January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October", "November", "December")
        var calProgressAnimationDuration = 500L


        var HCField1Type : String = "sodium" //keep same name as FireBase
        var HCField2Type : String = "carbohydrate"//FieldType is used for Home Screen nutrition boxes
        var HCField3Type : String = "sugar"
        var morningStartHour = 4
        var afternoonStartHour = 12
        var nightStartHour = 18


    }
}

data class HCField (var nutritionName : String, var position : Int, var value : Int)//position 1-3