package com.example.trackrrv1

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentSettingsCustomizeHomeBinding


class SettingsCustomizeHomeFragment : Fragment() {
    private var _binding: FragmentSettingsCustomizeHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsCustomizeHomeBinding.inflate(inflater, container, false)
        initializeDragAndDrop()
        // Inflate the layout for this fragment



        (activity as MainActivity?)!!.endTransition()
        return binding.root
    }

    fun initializeDragAndDrop(){
        binding.customizeHomeSodiumNode.setOnLongClickListener {
            val clipText = "This is our ClipData Text"
            val item = ClipData.Item(clipText)
            val mineTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mineTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)

            it.visibility = View.VISIBLE
            true
        }

        val dragListener = View.OnDragListener { v, event ->
            when (event.action){
                DragEvent.ACTION_DRAG_STARTED ->{
                    Log.d("Settings", "STARTED")
                    event.clipDescription.hasMimeType((ClipDescription.MIMETYPE_TEXT_PLAIN))
                }
                DragEvent.ACTION_DRAG_ENTERED ->{
                    Log.d("Settings", "ENTERED")
                    requireView().invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    Log.d("Settings", "LOCATION")
                    true
                }
                DragEvent.ACTION_DRAG_EXITED ->{
                    Log.d("Settings", "EXITED")
                    requireView().invalidate()
                    true
                }
                DragEvent.ACTION_DROP ->{
                    Log.d("Settings", "DROP")
                    val item = event.clipData.getItemAt(0)
                    val dragData = item.text
                    Toast.makeText(this.context, dragData, Toast.LENGTH_SHORT).show()

                    requireView().invalidate()

                    val v = event.localState as ImageView
                    val owner = v.parent as ViewGroup
                    owner.removeView(v)
                    val destination = view as ConstraintLayout//The drop place
                    destination.addView(v)
                    v.visibility = View.VISIBLE
                    true
                }

                DragEvent.ACTION_DRAG_ENDED ->{
                    Log.d("Settings", "ENDED")
                    requireView().invalidate()
                    true
                }

                else -> {
                    Log.d("Settings", "ELSE")
                    false
                }
            }
        }
        binding.layoutnode1.setOnDragListener(dragListener)
        binding.layoutnode2.setOnDragListener(dragListener)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}