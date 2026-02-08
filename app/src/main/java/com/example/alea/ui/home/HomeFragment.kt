package com.example.alea.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.R
import com.example.alea.databinding.FragmentHomeBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var challengesAdapter: ChallengesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupVictoryChart()
        setupClickListeners()
        observeState()
    }

    private fun setupRecyclerView() {
        challengesAdapter = ChallengesAdapter { challenge ->
            val action = HomeFragmentDirections.actionHomeToChallengeDetail(challenge.id)
            findNavController().navigate(action)
        }

        binding.challengesRecyclerView.apply {
            adapter = challengesAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupVictoryChart() {
        val chart = binding.victoryChart

        // Styling
        chart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(false)
            setDrawGridBackground(false)
            setDrawBorders(false)

            // X-Axis
            xAxis.apply {
                isEnabled = false
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
            }

            // Left Y-Axis
            axisLeft.apply {
                isEnabled = false
                setDrawGridLines(false)
            }

            // Right Y-Axis
            axisRight.isEnabled = false

            setBackgroundColor(Color.TRANSPARENT)
            setViewPortOffsets(0f, 0f, 0f, 0f)
        }

        // Demo data - 7 days
        val entries = listOf(
            Entry(0f, 150f),
            Entry(1f, 200f),
            Entry(2f, 180f),
            Entry(3f, 250f),
            Entry(4f, 220f),
            Entry(5f, 300f),
            Entry(6f, 350f)
        )

        val primaryColor = ContextCompat.getColor(requireContext(), R.color.alea_primary_start)
        val primaryEndColor = ContextCompat.getColor(requireContext(), R.color.alea_primary_end)

        val dataSet = LineDataSet(entries, "Coins").apply {
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
            setDrawFilled(true)
            fillColor = primaryColor
            fillAlpha = 50
            color = primaryEndColor
            lineWidth = 3f
            setDrawCircles(false)
            setDrawValues(false)
            setDrawHighlightIndicators(false)
        }

        chart.data = LineData(dataSet)
        chart.invalidate()
        chart.animateX(1000)
    }

    private fun setupClickListeners() {
        binding.notificationButton.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notifications)
        }

        binding.newChallengeAction.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_createChallenge)
        }

        binding.findFriendsAction.setOnClickListener {
            findNavController().navigate(R.id.friendsFragment)
        }

        binding.leaderboardAction.setOnClickListener {
            findNavController().navigate(R.id.rankingFragment)
        }

        binding.viewAllText.setOnClickListener {
            // TODO: Navigate to all challenges
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.user?.let { user ->
                    binding.greetingText.text = getString(R.string.home_greeting, user.username)
                    binding.coinsAmount.text = NumberFormat.getNumberInstance(Locale.US)
                        .format(user.coins)

                    val trend = if (user.wins > 0) "+${(user.winRate / 10).toInt()}%" else "0%"
                    binding.trendBadge.text = trend
                }

                challengesAdapter.submitList(state.challenges)

                // Show weekly improvement
                binding.improvementText.text = "+150₳ this week"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
