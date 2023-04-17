package com.example.trackrrv1

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutBinding

class FoodViewHolder(val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentFood: Food
    lateinit var authorString : String

    fun bindBook(food : Food) {
        currentFood = food

        val title = currentFood.foodName
        val subtitle = currentFood.calories
        val author = currentFood.author

        if (subtitle.equals("")){
//          binding.SubtitleTextView.setPadding(0,0,0,0)
//          binding.SubtitleTextView.height = 1
            binding.SubtitleTextView.setVisibility(View.GONE)
        }
        authorString = ""
        for (e in author) {
            authorString = "$authorString, $e"
        } //fix
        authorString = authorString.substring(2, authorString.length)


        binding.AuthorTextView.text = authorString
        binding.SubtitleTextView.text = subtitle
        binding.TitleTextView.text = title

//        Glide.with(itemView).load(currentFood.imageUri).into(binding.BookImageView);




    }

//    init {
//        binding.root.setOnClickListener{ view ->
//            val bookUri = Uri.parse(currentFood.url)
//            val websiteIntent = Intent(Intent.ACTION_VIEW, bookUri)
//            itemView.context.startActivity(websiteIntent)
//
//        }
//    }



}