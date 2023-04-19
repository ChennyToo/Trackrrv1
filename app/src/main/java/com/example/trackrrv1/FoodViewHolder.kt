package com.example.trackrrv1

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackrrv1.databinding.ListItemLayoutBinding

class FoodViewHolder(val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentFood: Food

    fun bindBook(food : Food) {
        currentFood = food

        val name = currentFood.foodName
        val calories = currentFood.calories
        val fat = currentFood.fat
        val sugar = currentFood.sugar


//        binding.AuthorTextView.text = author.toString()
//        binding.SubtitleTextView.text = subtitle.toString()
//        binding.TitleTextView.text = title
        ////TODO bind these after making your xml

//        Glide.with(itemView).load(currentFood.imageUri).into(binding.BookImageView);

    }




}