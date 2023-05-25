package com.example.trackrrv1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.trackrrv1.databinding.ListItemLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FoodAdapter(var foods: MutableList<Food>, myFragment: MainFragment) : RecyclerView.Adapter<FoodViewHolder>() {
    var dbRef: DatabaseReference = Firebase.database.reference
    lateinit var binding: ListItemLayoutBinding
    var parentFragment = myFragment
    var pastHolder : FoodViewHolder? = null


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
                    R.id.CustomizeFoodItemButton ->{
                        //Code that ensures there is only one open list item at a time
                        if (pastHolder != null && pastHolder != holder && pastHolder!!.binding.CustomizeFoodItemButton.text == "opened"){
                            if(pastHolder!!.binding.customizeAnimation.speed < 0) {
                                pastHolder!!.binding.customizeAnimation.playAnimation()
                            }
                            else {
                                pastHolder!!.binding.customizeAnimation.reverseAnimationSpeed()
                                pastHolder!!.binding.customizeAnimation.playAnimation()
                            }
                            pastHolder!!.binding.CustomizeFoodItemButton.text = "closed"
                            pastHolder!!.binding.TrashFoodItemButton.visibility = View.INVISIBLE
                            pastHolder!!.binding.WriteFoodItemButton.visibility = View.INVISIBLE
                        }
                        //DO NOT REVERSE ANIMATION SPEED AFTER PLAYING

                        //RUNS IF CLOSED
                        if (holder.binding.CustomizeFoodItemButton.text.equals("closed")){
                            holder.binding.CustomizeFoodItemButton.text = "opened"
                            if(holder.binding.customizeAnimation.speed > 0){
                                holder.binding.customizeAnimation.playAnimation()
                            }

                            else {
                                holder.binding.customizeAnimation.reverseAnimationSpeed()
                                holder.binding.customizeAnimation.playAnimation()
                            }

                            holder.binding.TrashFoodItemButton.visibility = View.VISIBLE
                            holder.binding.WriteFoodItemButton.visibility = View.VISIBLE
                        }

                        //IF IT IS OPEN
                        else {
                            holder.binding.CustomizeFoodItemButton.text = "closed"
                            if(holder.binding.customizeAnimation.speed < 0){
                                holder.binding.customizeAnimation.playAnimation()
                            }

                            else{
                                holder.binding.customizeAnimation.reverseAnimationSpeed()
                                holder.binding.customizeAnimation.playAnimation()
                            }
                            holder.binding.TrashFoodItemButton.visibility = View.INVISIBLE
                            holder.binding.WriteFoodItemButton.visibility = View.INVISIBLE
                        }
                        pastHolder = holder //Assign past holder AFTER everything has occured



                    }

                    R.id.WriteFoodItemButton ->{
                        Log.d("MainActivity", "WRITE")

                    }

                    R.id.TrashFoodItemButton -> {
                        Log.d("MainActivity", "T${holder.bindingAdapterPosition}")

//                        holder.binding.customizeAnimation.reverseAnimationSpeed()
//                        holder.binding.customizeAnimation.playAnimation()
//                        parentFragment.lifecycleScope.launch() {
//                            this.run {
//                                delay(770L)
                                val positionalChange = holder.bindingAdapterPosition
                                MainFragment.removeItemInList(foods[holder.bindingAdapterPosition].foodName)
                                foods.removeAt(holder.bindingAdapterPosition)
                                notifyItemRemoved(positionalChange)
                                notifyItemRangeChanged(positionalChange, foods.size)
//                            }
//                    }


                    }
                }
            }

            binding.CustomizeFoodItemButton.setOnClickListener(buttonsClickListener)
            binding.WriteFoodItemButton.setOnClickListener(buttonsClickListener)
            binding.TrashFoodItemButton.setOnClickListener(buttonsClickListener)


    }

    override fun getItemCount(): Int {
        return foods.size
    }



    fun allowEditMode(){

    }


}