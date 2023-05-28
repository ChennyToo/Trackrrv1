package com.example.trackrrv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.trackrrv1.databinding.ActivityMainBinding
import com.example.trackrrv1.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var transition : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
 



        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,)
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,)






        _binding = ActivityMainBinding.inflate(layoutInflater)
        transition = binding.transitionAnimation
        transition.frame = 0
        transition.pauseAnimation()
        setContentView(binding.root)
    }


    override fun onBackPressed() {//Removes back button functionality
        
    }

    fun startTransition(){
        transition.playAnimation()
        lifecycleScope.launch(){
                delay(Constants.transitionStartTime)
                transition.pauseAnimation()//Pausing animation must be at the time the transition fully covers screen,
                                            // end transition should be called once Fragment is done loading
            }
        }


    fun endTransition(){
        lifecycleScope.launch(){
            delay(Constants.transitionEndTime)
            transition.resumeAnimation()
        }
    }


}