package com.example.alea

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.alea.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var tabIcons: List<ImageView>
    private var currentTab = 0

    // Fragment IDs mapped to tab indices
    private val tabDestinations = mapOf(
        0 to R.id.homeFragment,
        1 to R.id.friendsFragment,
        3 to R.id.settingsFragment,
        4 to R.id.profileFragment
    )

    // Destinations where tab bar should be hidden
    private val hideTabBarDestinations = setOf(
        R.id.chatFragment, R.id.createStep0Fragment, R.id.createStep1Fragment,
        R.id.createStep2Fragment, R.id.createStep3Fragment, R.id.createStep4Fragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        tabIcons = listOf(
            binding.tabHome, binding.tabFriends,
            binding.fabCreate, // FAB center
            binding.tabSettings, binding.tabProfile
        )

        setupTabListeners()

        // Listen for destination changes to update tab bar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Show/hide tab bar
            val shouldHide = destination.id in hideTabBarDestinations
            binding.floatingTabBar.visibility = if (shouldHide) View.GONE else View.VISIBLE

            // Update active tab based on current destination
            tabDestinations.forEach { (index, destId) ->
                if (destination.id == destId) {
                    selectTab(index, animate = false)
                }
            }
        }

        // Set initial state
        selectTab(0, animate = false)
    }

    private fun setupTabListeners() {
        binding.tabHome.setOnClickListener { navigateToTab(0) }
        binding.tabFriends.setOnClickListener { navigateToTab(1) }
        binding.fabCreate.setOnClickListener {
            // FAB → Navigate to create challenge
            navController.navigate(R.id.createStep0Fragment)
        }
        binding.tabSettings.setOnClickListener { navigateToTab(3) }
        binding.tabProfile.setOnClickListener { navigateToTab(4) }
    }

    private fun navigateToTab(index: Int) {
        if (index == currentTab) return
        val destId = tabDestinations[index] ?: return

        // Pop back to the root and navigate fresh
        navController.popBackStack(navController.graph.startDestinationId, false)
        if (destId != navController.graph.startDestinationId) {
            navController.navigate(destId)
        }
        selectTab(index, animate = true)
    }

    private fun selectTab(index: Int, animate: Boolean) {
        currentTab = index
        val activeColor = ContextCompat.getColor(this, R.color.color_primary_start)
        val inactiveColor = ContextCompat.getColor(this, R.color.white_60)

        tabIcons.forEachIndexed { i, icon ->
            if (i == 2) return@forEachIndexed // Skip FAB

            if (i == index) {
                icon.setColorFilter(activeColor)
                if (animate) {
                    icon.animate().scaleX(1.2f).scaleY(1.2f).setDuration(150)
                        .setInterpolator(OvershootInterpolator())
                        .withEndAction {
                            icon.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                        }.start()
                }
            } else {
                icon.setColorFilter(inactiveColor)
                icon.scaleX = 1f
                icon.scaleY = 1f
            }
        }
    }
}
