package com.example.trackrrv1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FoodAdapter(var foods: MutableList<Food>) : RecyclerView.Adapter<FoodViewHolder>() {
    var dbRef: DatabaseReference = Firebase.database.reference
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

                        val positionalChange = holder.bindingAdapterPosition
                        MainFragment.removeItemInList(foods[holder.bindingAdapterPosition].foodName)
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