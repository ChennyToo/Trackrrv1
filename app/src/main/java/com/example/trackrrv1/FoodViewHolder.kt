package com.example.trackrrv1

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trackrrv1.databinding.ListItemLayoutBinding

class FoodViewHolder(val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentFood: Food

    fun bindBook(food : Food) {
        currentFood = food

        val title = currentFood.foodName
        val subtitle = currentFood.calories
        val author = currentFood.fat


        binding.AuthorTextView.text = author.toString()
        binding.SubtitleTextView.text = subtitle.toString()
        binding.TitleTextView.text = title

        Glide.with(itemView).load(currentFood.imageUri).into(binding.BookImageView);

    }




}