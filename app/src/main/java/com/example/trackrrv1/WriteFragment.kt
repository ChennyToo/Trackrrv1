package com.example.trackrrv1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentWriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime


class WriteFragment : Fragment() {

    lateinit var dbRef: DatabaseReference
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbRef = Firebase.database.reference
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when(view.id){
                    R.id.LogButton -> LogAndNavigate()
                    R.id.BackButton -> binding.root.findNavController().navigate(R.id.action_writeFragment_to_mainFragment)
                }
            }
        binding.LogButton.setOnClickListener(buttonsClickListener)
        binding.BackButton.setOnClickListener(buttonsClickListener)

        // Inflate the layout for this fragment
        return binding.root
    }

    fun LogAndNavigate(){
        val name = binding.NameEdit.text.toString()
        val calorie = binding.CalorieEdit.text.toString().toInt()
        val protein = binding.ProteinEdit.text.toString().toInt()
        val carb = binding.CarbEdit.text.toString().toInt()
        val fat = binding.FatEdit.text.toString().toInt()
        val imageUrl = "https://www.iconsdb.com/icons/preview/red/x-mark-3-xxl.png"
        val newFood = Food(name?: "", calorie?: 0, fat?: 0, 0,
            0, protein?: 0, carb?: 0, LocalDateTime.now(), imageUrl)
        dbRef.child(LocalDateTime.now().year.toString())
            .child(LocalDateTime.now().month.toString())
            .child(LocalDateTime.now().dayOfMonth.toString())
            .child(name!!).setValue(newFood).addOnSuccessListener {
            MainFragment.refreshScreen = true
        }
        binding.root.findNavController().navigate(R.id.action_writeFragment_to_mainFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}