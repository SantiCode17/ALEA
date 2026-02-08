package com.example.alea

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.alea.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    // Destinations where bottom nav + FAB should be visible
    private val bottomNavDestinations = setOf(
        R.id.homeFragment,
        R.id.rankingFragment,
        R.id.friendsFragment,
        R.id.profileFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupWindowInsets()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Manual bottom nav handling for proper top-level navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val destinationId = when (item.itemId) {
                R.id.homeFragment -> R.id.homeFragment
                R.id.rankingFragment -> R.id.rankingFragment
                R.id.friendsFragment -> R.id.friendsFragment
                R.id.profileFragment -> R.id.profileFragment
                else -> return@setOnItemSelectedListener false
            }

            // Don't navigate if already on this destination
            if (navController.currentDestination?.id == destinationId) return@setOnItemSelectedListener true

            try {
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, inclusive = false, saveState = false)
                    .setLaunchSingleTop(true)
                    .setRestoreState(false)
                    .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                    .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                    .build()

                navController.navigate(destinationId, null, navOptions)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        // FAB for create challenge
        binding.fabCreateChallenge.setOnClickListener {
            navController.navigate(R.id.createChallengeFragment)
        }

        // Show/hide bottom navigation + FAB based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = destination.id in bottomNavDestinations
            binding.bottomNavigation.visibility = if (showBottomNav) View.VISIBLE else View.GONE
            binding.fabCreateChallenge.visibility = if (showBottomNav) View.VISIBLE else View.GONE

            // Sync bottom nav selected state
            if (showBottomNav) {
                val menuItemId = when (destination.id) {
                    R.id.homeFragment -> R.id.homeFragment
                    R.id.rankingFragment -> R.id.rankingFragment
                    R.id.friendsFragment -> R.id.friendsFragment
                    R.id.profileFragment -> R.id.profileFragment
                    else -> null
                }
                menuItemId?.let {
                    if (binding.bottomNavigation.selectedItemId != it) {
                        binding.bottomNavigation.selectedItemId = it
                    }
                }
            }
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, 0)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}