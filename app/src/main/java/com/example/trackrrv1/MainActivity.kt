package com.example.trackrrv1

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.trackrrv1.databinding.ActivityMainBinding
import com.example.trackrrv1.databinding.FragmentMainBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
    lateinit var startingTransitionSound : MediaPlayer
    lateinit var endingTransitionSound : MediaPlayer



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        globalActivity = this
        setSettings()
        startingTransitionSound = MediaPlayer.create(this, R.raw.whooshsound2)
        endingTransitionSound = MediaPlayer.create(this, R.raw.whooshsound1)

//        var userPref = applicationContext.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
//        userPref.edit().remove("username").apply()
//        Way to remove value from sharedpreferences, can be used to log out


        Constants.username = applicationContext.getSharedPreferences("UserPref", Context.MODE_PRIVATE).getString("username", "empty") ?: "empty"//is there was no username set before, the default value is "empty"
        Log.d("MainActivity", "${Constants.username}")
        if (Constants.username != "empty"){//If the user has already logged in on said device, they will start on home screen. If not, default starting position is login screen
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val inflater = navHostFragment!!.findNavController().navInflater
            val graph = inflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(R.id.homeFragment)
            val navController = navHostFragment.findNavController()
            navController.setGraph(graph, intent.extras)
            Constants.userDatabaseReference = Firebase.database.reference.child(Constants.username)
        }

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION,)
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION,)







        transition = binding.transitionAnimation
        transition.frame = 0
        transition.pauseAnimation()
        setContentView(binding.root)
    }


    override fun onBackPressed() {//Removes back button functionality
        
    }

    fun startTransition(){
        startingTransitionSound.seekTo(500)
        startingTransitionSound.start()
        transition.playAnimation()
        lifecycleScope.launch(){
                delay(Constants.transitionStartTime)
                transition.pauseAnimation()//Pausing animation must be at the time the transition fully covers screen,
                                            // end transition should be called once Fragment is done loading
            }
        }


    fun endTransition(delayTime: Long = Constants.transitionEndTimeQuick){
        endingTransitionSound.start()
        lifecycleScope.launch(){
            delay(delayTime)
            transition.resumeAnimation()
        }
    }

    fun setSettings(){
        val UserPref = applicationContext.getSharedPreferences("UserPref", Context.MODE_PRIVATE)
        Constants.HCField1Type = UserPref.getString("HCField1", "sodium") ?: "sodium"
        Constants.HCField2Type = UserPref.getString("HCField2", "carbohydrate") ?: "carbohydrate"
        Constants.HCField3Type = UserPref.getString("HCField3", "sugar") ?: "sugar"
        Constants.calorieIntake = UserPref.getInt("calorieIntake", 2000)
        Constants.proteinIntake = UserPref.getInt("proteinIntake", 50)
        Constants.carbIntake = UserPref.getInt("carbIntake", 300)
        Constants.sodiumIntake = UserPref.getInt("sodiumIntake", 2000)
        Constants.fatIntake = UserPref.getInt("fatIntake", 60)
        Constants.sugarIntake = UserPref.getInt("sugarIntake", 30)
    }

    companion object{
        lateinit var globalActivity : AppCompatActivity
    }




}