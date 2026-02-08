package com.example.alea.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.R
import com.example.alea.databinding.FragmentProfileBinding
import com.example.alea.ui.components.PerformanceChartView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var achievementsAdapter: AchievementsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAchievementsRecyclerView()
        setupClickListeners()
        setupFilterToggle()
        setupPerformanceChart()
        observeState()
    }

    private fun setupAchievementsRecyclerView() {
        achievementsAdapter = AchievementsAdapter()
        binding.achievementsRecyclerView.apply {
            adapter = achievementsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupPerformanceChart() {
        // Set demo data for the weekly performance chart
        val demoData = listOf(
            PerformanceChartView.PerformanceData("Mon", 3, false),
            PerformanceChartView.PerformanceData("Tue", 5, true),
            PerformanceChartView.PerformanceData("Wed", 2, false),
            PerformanceChartView.PerformanceData("Thu", 7, true),
            PerformanceChartView.PerformanceData("Fri", 4, false),
            PerformanceChartView.PerformanceData("Sat", 6, true),
            PerformanceChartView.PerformanceData("Sun", 3, false)
        )
        binding.performanceChart.setData(demoData)
        binding.performanceChart.animateChart()
    }

    private fun setupClickListeners() {
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }

        binding.editProfileButton.setOnClickListener {
            // TODO: Navigate to edit profile
        }
    }

    private fun setupFilterToggle() {
        binding.filterToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                viewModel.toggleFilter(checkedId == R.id.completedButton)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.user?.let { user ->
                    // Name and handle
                    binding.userName.text = user.displayName.ifEmpty { user.username }
                    binding.userHandle.text = getString(R.string.profile_handle_format, user.username.lowercase())

                    // Level and title badge
                    binding.levelBadge.text = getString(R.string.profile_level_format, user.level, user.title)

                    // Stats
                    binding.totalCoinsValue.text = formatNumber(user.coins)
                    binding.totalChallengesValue.text = user.totalChallenges.toString()
                    binding.winRateValue.text = getString(R.string.profile_percentage_format, user.winRate.toInt())
                    binding.rankValue.text = getString(R.string.profile_rank_format, user.rank)
                }

                // Update achievements
                achievementsAdapter.submitList(state.unlockedAchievements)
            }
        }
    }

    private fun formatNumber(number: Long): String {
        return NumberFormat.getNumberInstance(Locale.US).format(number)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
