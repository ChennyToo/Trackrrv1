package com.example.trackrrv1

import com.squareup.moshi.Json

class FoodsResponse {
    @Json(name = "foods")
    lateinit var foodsItemsList : List<FoodItem>
}

class FoodItem {
    @Json(name = "food_name")
    var name :String? = ""
    @Json(name = "nf_calories")
    var calorie :Int? = 0
    @Json(name = "nf_total_fat")
    var fat :Int? = 0
    @Json(name = "nf_sugars")
    var sugar :Int? = 0
    @Json(name = "nf_sodium")
    var sodium :Int? = 0
    @Json(name = "nf_protein")
    var protein :Int? = 0
    @Json(name = "nf_total_carbohydrate")
    var carbohydrate :Int? = 0
    @Json(name = "photo")
    lateinit var imageLinks : Thumbnail
}

class Thumbnail{
    @Json(name = "thumb")
    var thumbnail :String? = ""
}
