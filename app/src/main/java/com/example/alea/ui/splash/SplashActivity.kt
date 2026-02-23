package com.example.alea.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.alea.MainActivity
import com.example.alea.databinding.ActivitySplashBinding
import com.example.alea.ui.auth.AuthActivity
import com.example.alea.ui.onboarding.OnboardingActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateLogo()

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("alea_prefs", MODE_PRIVATE)
            val isFirstLaunch = prefs.getBoolean("is_first_launch", true)
            val isLoggedIn = prefs.getBoolean("is_logged_in", false)

            val destination = when {
                isFirstLaunch -> OnboardingActivity::class.java
                !isLoggedIn -> AuthActivity::class.java
                else -> MainActivity::class.java
            }
            startActivity(Intent(this, destination))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2500)
    }

    private fun animateLogo() {
        val scaleAnim = ScaleAnimation(
            0.5f, 1f, 0.5f, 1f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        ).apply { duration = 800 }

        val alphaAnim = AlphaAnimation(0f, 1f).apply { duration = 800 }

        val animSet = AnimationSet(true).apply {
            addAnimation(scaleAnim)
            addAnimation(alphaAnim)
        }
        binding.splashLogo.startAnimation(animSet)

        binding.splashTitle.alpha = 0f
        binding.splashTitle.animate().alpha(1f).setStartDelay(400).setDuration(600).start()
        binding.splashSubtitle.alpha = 0f
        binding.splashSubtitle.animate().alpha(1f).setStartDelay(600).setDuration(600).start()
    }
}
