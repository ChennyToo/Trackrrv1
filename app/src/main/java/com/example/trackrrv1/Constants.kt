package com.example.trackrrv1

class Constants {
    companion object{
        //TODO Implement Shared preferences so settings save
        var transitionStartTime : Long = 400L//Upon finishing starttime, that is when navigation should occur
        var transitionEndTime : Long = 400L
        var main_customizeAnimationTime : Long = 800L
        var calorieIntake : Int = 200
        var proteinIntake : Int = 50//update based on user preference
        var carbIntake : Int = 300
        var fatIntake : Int = 60
        var monthList : List<String> = listOf("January", "February", "March",
            "April", "May", "June", "July", "August", "September", "October", "November", "December")
        var calProgressAnimationDuration = 500L
        var HCField1Type : String = "sodium" //keep same name as FireBase
        var HCField2Type : String = "carbohydrate"//FieldType is used for Home Screen nutrition boxes
        var HCField3Type : String = "sugar"
        var morningStartHour = 4
        var afternoonStartHour = 13
        var nightStartHour = 18

    }
}

data class HCField (var nutritionName : String, var position : Int, var value : Int)//position 1-3