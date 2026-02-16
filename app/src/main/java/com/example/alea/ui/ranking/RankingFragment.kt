package com.example.alea.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alea.R
import com.example.alea.databinding.FragmentRankingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RankingViewModel by viewModels()
    private lateinit var rankingAdapter: RankingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToggle()
        observeState()
    }

    private fun setupRecyclerView() {
        rankingAdapter = RankingAdapter()
        binding.rankingRecyclerView.adapter = rankingAdapter
    }

    private fun setupToggle() {
        binding.periodToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                viewModel.togglePeriod(checkedId == R.id.weeklyButton)
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                val suffix = if (state.isWeekly) "wins" else "pts"

                // Update podium
                if (state.topThree.isNotEmpty()) {
                    state.topThree.getOrNull(0)?.let { first ->
                        binding.firstName.text = first.username
                        val value = if (state.isWeekly) first.wins.toLong() else first.coins
                        binding.firstPoints.text = "${formatNumber(value)} $suffix"
                    }
                    state.topThree.getOrNull(1)?.let { second ->
                        binding.secondName.text = second.username
                        val value = if (state.isWeekly) second.wins.toLong() else second.coins
                        binding.secondPoints.text = "${formatNumber(value)} $suffix"
                    }
                    state.topThree.getOrNull(2)?.let { third ->
                        binding.thirdName.text = third.username
                        val value = if (state.isWeekly) third.wins.toLong() else third.coins
                        binding.thirdPoints.text = "${formatNumber(value)} $suffix"
                    }
                }

                // Update your position card
                binding.yourPositionName.text = state.currentUserName.ifEmpty { "Tú" }
                binding.yourPositionNumber.text = "#${state.currentUserRank}"
                binding.yourPositionPoints.text = "${formatNumber(state.currentUserPoints)} $suffix"

                // Update list
                rankingAdapter.submitList(state.restOfRanking)
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
