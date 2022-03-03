package com.example.nytimes.presentation.ui.features.splash

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.example.nytimes.R
import com.example.nytimes.databinding.ActivitySplashBinding
import com.example.nytimes.presentation.ui.features.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding
    private val timerFuture: Long = 2000
    private val timerCountDown: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        /**
         * Timer set to show transition animation to MainActivity.kt
         */
        object : CountDownTimer(timerFuture, timerCountDown) {
            override fun onFinish() {
                val goToMainActivity = Intent(applicationContext, MainActivity::class.java)
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this@SplashActivity,
                    binding?.appLogo,
                    getString(R.string.transition_layout)
                )

                startActivity(goToMainActivity, activityOptions.toBundle())

                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_STOP) {
                            lifecycle.removeObserver(this)
                            finish()
                        }
                    }
                })
            }

            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }
}