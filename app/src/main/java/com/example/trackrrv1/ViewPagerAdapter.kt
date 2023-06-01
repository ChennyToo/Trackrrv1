package com.example.trackrrv1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.NutrientViewPagerBinding

class ViewPagerAdapter(val images: List<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    lateinit var binding: NutrientViewPagerBinding

    inner class ViewPagerViewHolder(val bind : NutrientViewPagerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindImage(image : Int){
            bind.goalsNutrientImage.setBackgroundResource(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = NutrientViewPagerBinding.inflate(layoutInflater, parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val curImage = images[position]
        holder.bindImage(curImage)
    }

}