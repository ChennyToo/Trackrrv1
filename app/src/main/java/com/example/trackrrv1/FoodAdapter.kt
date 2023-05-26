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
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FoodAdapter(var foods: MutableList<Food>, myFragment: MainFragment) :
    RecyclerView.Adapter<FoodViewHolder>() {
    var dbRef: DatabaseReference = Firebase.database.reference
    lateinit var binding: ListItemLayoutBinding
    var parentFragment = myFragment
    var pastHolder: FoodViewHolder? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ListItemLayoutBinding.inflate(layoutInflater, parent, false)

        return FoodViewHolder(binding)

    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.bindFood(food)
//        Log.d("TrySail", "${food.foodName}${holder.bindingAdapterPosition}")

        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.cardBG2 ->{
//                        Log.d("MainActivity", "${holder.bindingAdapterPosition}")
                    }

                    R.id.CustomizeFoodItemButton -> {
                        //Code that ensures there is only one open list item at a time, second condition ensures that clicking the same item twice does not run this condition and rest of code on the same item.
                        //Third condition ensures that this code only runs when the menu is opened, or else it will run the animation when closed
                        if (pastHolder != null && pastHolder != holder && pastHolder!!.binding.CustomizeFoodItemButton.text == "opened") {
                            if (pastHolder!!.binding.customizeAnimation.speed < 0) {//If the lottie is reversed, play it so that it closes
                                pastHolder!!.binding.customizeAnimation.playAnimation()
                            } else {
                                pastHolder!!.binding.customizeAnimation.reverseAnimationSpeed()
                                pastHolder!!.binding.customizeAnimation.playAnimation()
                            }
                            pastHolder!!.binding.CustomizeFoodItemButton.text = "closed"//Set the state of the Button to closed
                            pastHolder!!.binding.TrashFoodItemButton.visibility = View.INVISIBLE //Disable the ability to click on the buttons as they are not on screen anymore
                            pastHolder!!.binding.WriteFoodItemButton.visibility = View.INVISIBLE
                        }

                        //RUNS IF CLOSED
                        if (holder.binding.CustomizeFoodItemButton.text.equals("closed")) {
                            holder.binding.CustomizeFoodItemButton.text = "opened"//set state to open
                            if (holder.binding.customizeAnimation.speed > 0) {//if animation is normal speed, play
                                holder.binding.customizeAnimation.playAnimation()
                            } else {
                                holder.binding.customizeAnimation.reverseAnimationSpeed()//else, make it go from reverse to normal and then play
                                holder.binding.customizeAnimation.playAnimation()
                            }



                            holder.binding.TrashFoodItemButton.visibility = View.VISIBLE//Make the two buttons able to be clicked when animation begins opening
                            holder.binding.WriteFoodItemButton.visibility = View.VISIBLE
                        }

                        //IF IT IS OPEN
                        else {
                            holder.binding.CustomizeFoodItemButton.text = "closed"
                            if (holder.binding.customizeAnimation.speed < 0) {
                                holder.binding.customizeAnimation.playAnimation()
                            } else {
                                holder.binding.customizeAnimation.reverseAnimationSpeed()
                                holder.binding.customizeAnimation.playAnimation()
                            }

                            holder.binding.TrashFoodItemButton.visibility = View.INVISIBLE
                            holder.binding.WriteFoodItemButton.visibility = View.INVISIBLE
                            Log.d("1MainActivity", "IsNotVISIBLE")

                        }
                        holder.binding.CustomizeFoodItemButton.isClickable = false //Prevents user from pressing the button to glitch out the animation by making them wait

                        parentFragment.lifecycleScope.launch {
                            this.run{
                                delay(Constants.main_customizeAnimationTime)
                                holder.binding.CustomizeFoodItemButton.isClickable = true
                            }
                        }
                        pastHolder = holder //Assign past holder AFTER everything has occured because holder changes greatly throughout





                    }

                    R.id.WriteFoodItemButton -> {
                        holder.binding.WriteFoodItemButton.isClickable = false//Ensures that they do not run the same function again

                    }

                    R.id.TrashFoodItemButton -> {
                        Log.d("MainActivity", "${holder.bindingAdapterPosition}")
                        holder.binding.TrashFoodItemButton.isClickable = false //Ensures that they do not run the same function again
                        holder.binding.customizeAnimation.reverseAnimationSpeed()//Animation must always be reversed as the customize button is OPEN
                        holder.binding.customizeAnimation.playAnimation()

                        parentFragment.lifecycleScope.launch() {
                            this.run {
                                parentFragment.binding.mainClickBlocker.visibility =
                                    View.VISIBLE//Does not allow user to click anything until item is fully deleted
                                delay(Constants.main_customizeAnimationTime)
                                if (parentFragment != null) {
                                    //Code that will shift the items to replace the deleted item as well as update FireBase of this deletion
                                    val positionalChange =
                                        holder.bindingAdapterPosition
                                    //TODO May have to utilize positional change if code breaks
                                    MainFragment.removeItemInList(foods[holder.bindingAdapterPosition].foodName)
                                    foods.removeAt(holder.bindingAdapterPosition)
                                    notifyItemRemoved(holder.bindingAdapterPosition)
//                                    notifyDataSetChanged()//Good fix but does not do animation
                                    notifyItemRangeChanged(
                                        holder.bindingAdapterPosition,
                                        itemCount
                                    )
                                    holder.itemView.visibility = View.GONE
                                    parentFragment.binding.mainClickBlocker.visibility =
                                        View.INVISIBLE//Click privilage is back
                                    parentFragment.binding.amountLoggedTV.text = "You have logged ${foods.size} items"
                                    Log.d("MainActivity", "${foods.forEach { food ->  Log.d("MainActivity", "$food")}}")
                                }
                            }
                        }


                    }
                }
            }

        binding.CustomizeFoodItemButton.setOnClickListener(buttonsClickListener)
        binding.WriteFoodItemButton.setOnClickListener(buttonsClickListener)
        binding.TrashFoodItemButton.setOnClickListener(buttonsClickListener)
        binding.cardBG2.setOnClickListener(buttonsClickListener)


    }

    override fun getItemCount(): Int {
        return foods.size
    }


    fun allowEditMode() {

    }


}