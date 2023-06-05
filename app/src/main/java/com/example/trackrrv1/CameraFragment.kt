package com.example.trackrrv1


import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.trackrrv1.databinding.FragmentCameraBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FoodViewModel by activityViewModels()
    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        viewModel.foodFromCamera.value = Food("initializedFoodFromCamera")
        var getBarcodeSound = MediaPlayer.create(requireContext(), R.raw.camera_getbarcodesound)
        val scannerView = binding.scannerView
        codeScanner = CodeScanner(this.requireContext(), scannerView)
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                1
            )
        }


// Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE// or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        initializeFailureOrSuccessFunctionality()
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            getBarcodeSound.seekTo(0)
            getBarcodeSound.start()
            requireActivity().runOnUiThread {
                var UPC = it.text.toLong()
                viewModel.getFoods(UPC)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                Toast.makeText(
                    this.requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        val buttonsClickListener: View.OnClickListener =
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.BackCameraButton -> {
                        binding.BackCameraButton.isClickable = false
                        (activity as MainActivity?)!!.startTransition()
                        lifecycleScope.launch() {
                            delay(Constants.transitionStartTime)
                            binding.root.findNavController()
                                .navigate(R.id.action_cameraFragment_to_mainFragment)
                        }
                    }
                }
            }

        binding.BackCameraButton.setOnClickListener(buttonsClickListener)
        (activity as MainActivity?)!!.endTransition(Constants.transitionEndTime)//End the transition once everything else has been initialized

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initializeFailureOrSuccessFunctionality(){
        viewModel.foodFromCamera.observe(viewLifecycleOwner) { food -> //Upon setting an observer, the ViewModel will run the method immediately but instead
            if(viewModel.foodFromCamera.value!!.foodName == "empty"){
                lifecycleScope.launch{
                    Log.d("CameraFragment", "Error scanning barcode")
                    //Snackbar here
                    Snackbar.make(binding.root, "Barcode does not work!", Snackbar.LENGTH_SHORT)
                        .setAction("OK") {
                            //Click listener makes it so that user can click anywhere on screen to dismiss snackbar
                            codeScanner.startPreview()//StartPreview resets the camera to unfreeze it
                        }.show()
                    delay(1500)
                    codeScanner.startPreview()




                }

            }
            else if (viewModel.foodFromCamera.value!!.foodName != "empty" && viewModel.foodFromCamera.value!!.foodName != "initializedFoodFromCamera") {//We want it to navigate AFTER the ViewModel processes the API data and sets the foodFromCamera variable
                lifecycleScope.launch {
                    (activity as MainActivity?)!!.startTransition()
                    delay(Constants.transitionStartTime)
                    val navigateToWriteFragWithFood =
                        CameraFragmentDirections.actionCameraFragmentToWriteFragment(
                            food
                        )
                    binding.root.findNavController()
                        .navigate(navigateToWriteFragWithFood)
                    Log.d("CameraFragment", "Navigating")
                }
            }
            else {
                Log.d("CameraFragment", "Error with result functionality")
            }
        }
    }



    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel()
    }




}