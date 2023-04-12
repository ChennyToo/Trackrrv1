package com.example.googlebooksnetworking

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googlebooksnetworking.databinding.ListItemLayoutBinding

class BookViewHolder(val binding: ListItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var currentBook: Book
    lateinit var authorString : String

    fun bindBook(book : Book) {
        currentBook = book

        val title = currentBook.title
        val subtitle = currentBook.subtitle
        val author = currentBook.author

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

        Glide.with(itemView).load(currentBook.imageUri).into(binding.BookImageView);




    }

    init {
        binding.root.setOnClickListener{ view ->
            val bookUri = Uri.parse(currentBook.url)
            val websiteIntent = Intent(Intent.ACTION_VIEW, bookUri)
            itemView.context.startActivity(websiteIntent)

        }
    }



}