package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alea.databinding.StepChallengeFinalBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class StepChallengeFinalFragment : Fragment() {

    private var _binding: StepChallengeFinalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateChallengeViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StepChallengeFinalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBetControls()
        setupCalendar()
        observeState()
    }

    private fun setupBetControls() {
        binding.decreaseButton.setOnClickListener {
            val current = viewModel.uiState.value.betAmount
            viewModel.updateBetAmount(current - 10)
        }

        binding.increaseButton.setOnClickListener {
            val current = viewModel.uiState.value.betAmount
            viewModel.updateBetAmount(current + 10)
        }
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = java.util.Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            viewModel.updateDeadline(calendar.time)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.betAmountText.text = state.betAmount.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
