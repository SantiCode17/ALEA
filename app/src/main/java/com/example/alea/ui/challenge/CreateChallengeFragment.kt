package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentCreateChallengeBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateChallengeFragment : Fragment() {

    private var _binding: FragmentCreateChallengeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupClickListeners()
        observeState()
    }

    private fun setupViewPager() {
        binding.stepsViewPager.isUserInputEnabled = false
        binding.stepsViewPager.adapter = CreateChallengeStepsAdapter(this)
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            val currentStep = viewModel.uiState.value.currentStep
            if (currentStep > 0) {
                viewModel.previousStep()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.nextButton.setOnClickListener {
            val currentStep = viewModel.uiState.value.currentStep
            if (currentStep < 2) {
                viewModel.nextStep()
            }
        }

        binding.createButton.setOnClickListener {
            viewModel.createChallenge()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Update ViewPager
                binding.stepsViewPager.currentItem = state.currentStep

                // Update progress indicators
                updateProgressIndicators(state.currentStep)

                // Toggle between next button and create button for last step
                val isLastStep = state.currentStep == 2
                binding.nextButton.isVisible = !isLastStep
                binding.createButton.isVisible = isLastStep

                // Loading state
                binding.createButton.isEnabled = !state.isLoading
                if (state.isLoading) {
                    binding.createButton.text = getString(R.string.create_loading)
                } else {
                    binding.createButton.text = getString(R.string.create_button)
                }

                // Handle success - navigate to victory screen
                if (state.isSuccess) {
                    viewModel.onSuccessConsumed()
                    findNavController().navigate(R.id.action_createChallenge_to_victory)
                }

                // Handle error with Snackbar
                state.error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(resources.getColor(R.color.alea_error, null))
                        .setTextColor(resources.getColor(R.color.white, null))
                        .show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun updateProgressIndicators(step: Int) {
        val activeColor = R.drawable.gradient_primary
        val inactiveColor = R.color.alea_surface_variant

        binding.step1Indicator.setBackgroundResource(if (step >= 0) activeColor else inactiveColor)
        binding.step2Indicator.setBackgroundResource(if (step >= 1) activeColor else inactiveColor)
        binding.step3Indicator.setBackgroundResource(if (step >= 2) activeColor else inactiveColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
