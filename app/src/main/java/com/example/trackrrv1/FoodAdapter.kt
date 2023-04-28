package com.example.trackrrv1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutBinding


class FoodAdapter(var foods: MutableList<Food>) : RecyclerView.Adapter<FoodViewHolder>() {

    lateinit var binding: ListItemLayoutBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemLayoutBinding.inflate(layoutInflater, parent, false)

        return FoodViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.bindFood(food)
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.DeleteFoodItemButton -> {
                        Log.d("MainActivity", "${holder.bindingAdapterPosition}")
                        val positionalChange = holder.bindingAdapterPosition
                        foods.removeAt(holder.bindingAdapterPosition)
                        notifyItemRemoved(positionalChange)
                        notifyItemRangeChanged(positionalChange, foods.size)
                    }
                }
            }
            binding.DeleteFoodItemButton.setOnClickListener(buttonsClickListener)

    }

    override fun getItemCount(): Int {
        return foods.size
    }
}