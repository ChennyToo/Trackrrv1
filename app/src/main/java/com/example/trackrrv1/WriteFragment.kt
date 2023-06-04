package com.example.trackrrv1

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Gallery
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.trackrrv1.databinding.FragmentLogInBinding
import com.example.trackrrv1.databinding.FragmentWriteBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class WriteFragment : Fragment() {
    private val foodArgs by navArgs<WriteFragmentArgs>()

    var dbRef: DatabaseReference = Constants.userDatabaseReference
    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()
    private var isEditState = false
    private var mStorageRef: StorageReference =
        FirebaseStorage.getInstance().getReference("uploads")
    lateinit var foodName: String
    lateinit var foodTimeIdentifier: String
    lateinit var imageUri: Uri
    lateinit var logSound : MediaPlayer
    var didChangeImage = false
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =

        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null && result.data != null) {
                // getting URI of selected Image
                imageUri = result.data?.data!!
                Glide.with(this).load(imageUri).into(binding.writeFoodImage)
                didChangeImage = true

            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isEditState = false
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        logSound = MediaPlayer.create(requireContext(), R.raw.write_logfoodsound)

        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.LogButton -> {
                        checkFieldValidity()
                    }

                    R.id.writeBackScreenButton -> {
                        disableButtonFunctionality()
                        (activity as MainActivity?)!!.startTransition()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_writeFragment_to_mainFragment)
                        }
                    }

                    R.id.writeGalleryButton -> {


                        // PICK INTENT picks item from data
                        // and returned selected item
                        val galleryIntent = Intent(Intent.ACTION_PICK)
                        // here item is type of image
                        galleryIntent.type = "image/*"
                        // ActivityResultLauncher callback
                        //request code is anything really, just used as a identifier
//                        var soemthing = startActivityForResult(galleryIntent, 1)
                        imagePickerActivityResult.launch(galleryIntent)

                    }
                }
            }
        binding.LogButton.setOnClickListener(buttonsClickListener)
        binding.writeBackScreenButton.setOnClickListener(buttonsClickListener)
        binding.writeGalleryButton.setOnClickListener(buttonsClickListener)
        if (foodArgs.foodItemPassedInFromEdit.foodName != "empty") {
            isEditState = true
            changeToEditState(foodArgs.foodItemPassedInFromEdit)
        }
        (activity as MainActivity?)!!.endTransition()//finish the transition after everything else is done

        // Inflate the layout for this fragment
        return binding.root
    }

    fun LogAndNavigate() {
        val name = binding.NameEdit.text.toString()
        foodName = name
        val calorie = if (binding.CalorieEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.CalorieEdit.text.toString().toInt()
        }
        val protein = if (binding.ProteinEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.ProteinEdit.text.toString().toInt()
        }
        val carb = if (binding.CarbEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.CarbEdit.text.toString().toInt()
        }
        val fat = if (binding.FatEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.FatEdit.text.toString().toInt()
        }
        val sugar = if (binding.SugarEdit.text.toString().isEmpty()) {
            0
        } else {
            binding.SugarEdit.text.toString().toInt()
        }
        //Issue

        val time = if (isEditState) {
            foodArgs.foodItemPassedInFromEdit.timeLogged
        } else {
            LocalDateTime.now()
        }
        val uriStringFromCameraOrEdit = foodArgs.foodItemPassedInFromEdit.imageUriString

        foodTimeIdentifier = "${time.hour}${time.minute}"
        val newFood = Food(
            name ?: "", calorie ?: 0, fat ?: 0, sugar,
            0, protein ?: 0, carb ?: 0, time, uriStringFromCameraOrEdit
        )

        if (didChangeImage) {//if the user did not select an image for the food item, dont attempt to upload to Firebase
            //Add the image that user uploaded to Firebase storage, the delete line is meant to remove an image if they had already set one previously
            newFood.imageUriString = ""
            Log.d("WriteFragment", "Image changed")
            val deleteTask =
                mStorageRef.child("${Constants.username}/${foodName}${foodTimeIdentifier}").delete()
            val uploadTask =
                mStorageRef.child("${Constants.username}/${foodName}${foodTimeIdentifier}")
                    .putFile(imageUri!!)
        }

        dbRef.child(LocalDateTime.now().year.toString())
            .child(LocalDateTime.now().month.toString())
            .child(foodArgs.foodItemPassedInFromEdit.timeLogged.dayOfMonth.toString())
            .child(foodArgs.foodItemPassedInFromEdit.foodName)
            .ref.removeValue()
        dbRef.child(LocalDateTime.now().year.toString())
            .child(LocalDateTime.now().month.toString())
            .child(
                if (isEditState) {
                    foodArgs.foodItemPassedInFromEdit.timeLogged.dayOfMonth.toString()
                } else {
                    LocalDateTime.now().dayOfMonth.toString()
                }
            )//Editing a food item while changing days will update yesterday's date
            .child(name!!).setValue(newFood).addOnSuccessListener {
                (activity as MainActivity?)!!.startTransition()
                lifecycleScope.launch() {
                    delay(Constants.transitionStartTime)
                    binding.root.findNavController()
                        .navigate(R.id.action_writeFragment_to_mainFragment)
                }
            }.addOnFailureListener {
                Log.d("WriteFragment", "Item failed to update Firebase")
            }

    }

    fun changeToEditState(foodItem: Food) {
        if (foodItem.imageUriString != "") {
            Glide.with(this).load(foodItem.imageUriString.toUri())
                .into(binding.writeFoodImage)
        } else {
            mStorageRef.child("${Constants.username}/${foodItem.foodName}${foodItem.timeLogged.hour}${foodItem.timeLogged.minute}").downloadUrl.addOnSuccessListener {
                Glide.with(this)
                    .load(it)
                    .into(binding.writeFoodImage)
                Log.d("FoodViewHolder", "download passed")
            }
        }
        binding.NameEdit.setText(foodItem.foodName)
        binding.CalorieEdit.setText(foodItem.calories.toString())
        binding.ProteinEdit.setText(foodItem.protein.toString())
        binding.CarbEdit.setText(foodItem.carbohydrate.toString())
        binding.FatEdit.setText(foodItem.fat.toString())
        binding.SugarEdit.setText(foodItem.sugar.toString())
        binding.SodiumEdit.setText(foodItem.sodium.toString())
    }

    fun checkFieldValidity() {
        val name = binding.NameEdit.text.toString()
        if (name.contains(".")
            || name.contains("#")
            || name.contains("$")
            || name.contains("[")
            || name.contains("]")
            || name.isEmpty()
        ) {
            displayErrorToUser()
        } else {
            logSound.start()
            disableButtonFunctionality()
            LogAndNavigate()
        }
    }

    fun displayErrorToUser() {
        Log.d("WriteFragment", "displayErrorToUser name field has bad characters")
        Toast.makeText(requireActivity(), "Invalid Food Name!", Toast.LENGTH_SHORT).show()
    }

    fun disableButtonFunctionality() {
        binding.writeBackScreenButton.isClickable = false
        binding.LogButton.isClickable = false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}