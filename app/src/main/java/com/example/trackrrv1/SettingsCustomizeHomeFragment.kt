package com.example.trackrrv1

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentSettingsCustomizeHomeBinding
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SettingsCustomizeHomeFragment : Fragment() {
    private var _binding: FragmentSettingsCustomizeHomeBinding? = null
    private val binding get() = _binding!!
    var Position1Value: String = ""
    var Position2Value: String = ""
    var Position3Value: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingsCustomizeHomeBinding.inflate(inflater, container, false)
        initializeDragAndDrop()
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.customizeHomeConfirmButton -> {
                        updateHomeNutrientsAndNavigateBack()
                    }

                    R.id.customizeHomeRefreshButton -> {
                        resetValuesAndNodesBackToOriginal()
                    }

                    R.id.customizeBackButton ->{
                        removeButtonsFunctionality()
                        (activity as MainActivity?)!!.startTransition()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_settingsCustomizeHomeFragment_to_settingsFragment)
                        }
                    }
                }
            }

        binding.customizeHomeConfirmButton.setOnClickListener(buttonsClickListener)
        binding.customizeHomeRefreshButton.setOnClickListener(buttonsClickListener)
        binding.customizeBackButton.setOnClickListener(buttonsClickListener)

        // Inflate the layout for this fragment


//        (activity as MainActivity?)!!.endTransition()
        return binding.root
    }

    fun initializeDragAndDrop() {
        binding.customizeHomeSodiumNode.setOnLongClickListener {
            val clipText = "sodium"
            val item = ClipData.Item(clipText)
            val mineTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mineTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        binding.customizeHomeProteinNode.setOnLongClickListener {
            val clipText = "protein"
            val item = ClipData.Item(clipText)
            val mineTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mineTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        binding.customizeHomeCarbNode.setOnLongClickListener {
            val clipText = "carbohydrate"
            val item = ClipData.Item(clipText)
            val mineTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mineTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        binding.customizeHomeFatNode.setOnLongClickListener {
            val clipText = "fat"
            val item = ClipData.Item(clipText)
            val mineTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mineTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }
        binding.customizeHomeSugarNode.setOnLongClickListener {
            val clipText = "sugar"
            val item = ClipData.Item(clipText)
            val mineTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mineTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.INVISIBLE
            true
        }

        val dragListener =
            View.OnDragListener { nodeHolder, event -> //parameter1 refers to the views assigned to the dragListener
                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        Log.d("Settings", "STARTED")
                        event.clipDescription.hasMimeType((ClipDescription.MIMETYPE_TEXT_PLAIN))
                    }

                    DragEvent.ACTION_DRAG_ENTERED -> {
                        Log.d("Settings", "ENTERED")
                        requireView().invalidate()
                        true
                    }

                    DragEvent.ACTION_DRAG_LOCATION -> {
                        Log.d("Settings", "LOCATION")
                        true
                    }

                    DragEvent.ACTION_DRAG_EXITED -> {
                        Log.d("Settings", "EXITED")
                        requireView().invalidate()
                        true
                    }

                    DragEvent.ACTION_DROP -> {
                        Log.d("Settings", "DROP")
                        val item = event.clipData.getItemAt(0)
                        val dragData = item.text //the text data that the node carries (i.e sodium)

                        requireView().invalidate()

                        val node = event.localState as ImageView

                        val owner =
                            node.parent as ConstraintLayout //original view that held the node
                        owner.removeView(node)

                        var destination =
                            nodeHolder as ConstraintLayout //destination view that will hold the node
                        destination.addView(node)
                        destination.setOnDragListener(null)//removes ability for more nodes to be placed one the spot is held
                        setNodeHolderValue(
                            nodeHolder,
                            dragData.toString()
                        )//updates one of the three positions with the food value (i.e Position1 = "sodium" if sodium node is passed into holder1)
                        node.visibility = View.VISIBLE
                        true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {//Make it so that when the node is in a holder and is dragged outside, it goes back to it's parent holder
                        Log.d("Settings", "ENDED")
                        val node = event.localState as ImageView //v in this case would be the node
//                    if (node.parent == binding.customizeNodeHolder1){
//                        val owner = node.parent as ConstraintLayout //original view that held the node
//                        owner.removeView(node)
//
//                        var originalSpot = binding.sodiumHolder as ConstraintLayout //destination view that will hold the node
//                        originalSpot.addView(node)
//                    }

                        requireView().invalidate()
                        node.visibility = View.VISIBLE
                        true
                    }

                    else -> {
                        Log.d("Settings", "ELSE")
                        false
                    }
                }
            }
        binding.customizeNodeHolder1.setOnDragListener(dragListener)
        binding.customizeNodeHolder2.setOnDragListener(dragListener)
        binding.customizeNodeHolder3.setOnDragListener(dragListener)
        (activity as MainActivity?)!!.endTransition()
    }

    fun setNodeHolderValue(destination: ConstraintLayout, value: String) {
        if (destination == binding.customizeNodeHolder1) {
            binding.customizeHomeHolder1Animation.playAnimation()
            binding.customizeHomeHolder1Label.visibility = View.INVISIBLE
            Position1Value = value
            Log.d("CustomizeHome", "$value")
        } else if (destination == binding.customizeNodeHolder2) {
            binding.customizeHomeHolder2Animation.playAnimation()
            binding.customizeHomeHolder2Label.visibility = View.INVISIBLE
            Position2Value = value
            Log.d("CustomizeHome", "$value")
        } else if (destination == binding.customizeNodeHolder3) {
            binding.customizeHomeHolder3Animation.playAnimation()
            binding.customizeHomeHolder3Label.visibility = View.INVISIBLE
            Position3Value = value
            Log.d("CustomizeHome", "$value")
        } else {
            Log.d("CustomizeHome", "getNodeHolderValues no valid destination")
        }
    }

    fun updateHomeNutrientsAndNavigateBack() {
        if (!(Position1Value.isEmpty() || Position2Value.isEmpty() || Position3Value.isEmpty())) {//If all three fields are holding a nutrient, user is allowed to set settings and navigate
            Constants.HCField1Type = Position1Value
            Constants.HCField2Type = Position2Value
            Constants.HCField3Type = Position3Value

            //Update sharedPreferences here
            var sharedPref = requireActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor = sharedPref.edit()
            editor.putString("HCField1", Position1Value)
            editor.putString("HCField2", Position2Value)
            editor.putString("HCField3", Position3Value)
            editor.commit()



            (activity as MainActivity?)!!.startTransition() //How to call methods in MainActivity
//            removeAllButtonFunctionality() //prevents the user from clicking once navigation starts
            lifecycleScope.launch() {
                delay(Constants.transitionStartTime)
                binding.root.findNavController()
                    .navigate(R.id.action_settingsCustomizeHomeFragment_to_settingsFragment)
            }
        } else {
            Log.d("CustomizeHome", "updateHomeNutrients ERROR Atleast One position value is empty")
        }
    }

    fun resetValuesAndNodesBackToOriginal() {
        binding.root.findNavController()
            .navigate(R.id.action_settingsCustomizeHomeFragment_self)
    }

    private fun removeButtonsFunctionality(){
        binding.customizeHomeConfirmButton.setOnClickListener(null)
        binding.customizeHomeRefreshButton.setOnClickListener(null)
        binding.customizeBackButton.setOnClickListener(null)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.cancel()
        _binding = null
    }
}