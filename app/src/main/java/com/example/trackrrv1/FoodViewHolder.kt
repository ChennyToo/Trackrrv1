package com.example.trackrrv1

import android.view.View
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackrrv1.databinding.ListItemLayoutBinding

class FoodViewHolder(val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentFood: Food



    fun bindFood(food : Food) {
        currentFood = food

        val name = currentFood.foodName
        val calories = currentFood.calories
        val fat = currentFood.fat
        val sugar = currentFood.sugar
        val protein = currentFood.protein
        val carb = currentFood.carbohydrate
//TODO CHANGE THE NAMES
        binding.CalorieTextView.text = "$calories"
        binding.nutrientTV3.text = "Fat: ${fat}g"
        binding.NameTextView.text = name
        binding.nutrientTV1.text = "Protein: ${protein}g"
        binding.nutrientTV2.text = "Carbs: ${carb}g"
        Glide.with(itemView).load(currentFood.imageUriString.toUri()).into(binding.FoodImageView);

//        binding.AuthorTextView.text = author.toString()
//        binding.SubtitleTextView.text = subtitle.toString()
//        binding.TitleTextView.text = title
        ////TODO bind these after making your xml

//        Glide.with(itemView).load(currentFood.imageUri).into(binding.BookImageView);

    }




}