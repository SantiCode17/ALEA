package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.alea.R
import com.example.alea.data.model.ChallengeStatus
import com.example.alea.databinding.FragmentChallengeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChallengeDetailFragment : Fragment() {

    private var _binding: FragmentChallengeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeDetailViewModel by viewModels()
    private val args: ChallengeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadChallenge(args.challengeId)
        setupClickListeners()
        observeState()
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.completeButton.setOnClickListener {
            viewModel.completeChallenge()
        }

        binding.acceptButton.setOnClickListener {
            viewModel.acceptChallenge()
        }

        binding.declineButton.setOnClickListener {
            viewModel.declineChallenge()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Show/hide loading
                binding.loadingIndicator.isVisible = state.isLoading
                binding.contentContainer.isVisible = !state.isLoading

                state.challenge?.let { challenge ->
                    // Basic info
                    binding.titleText.text = challenge.title
                    binding.descriptionText.text = challenge.description.ifEmpty {
                        getString(R.string.challenge_no_description)
                    }
                    binding.betAmountText.text = challenge.betAmount.toString()
                    binding.creatorName.text = challenge.creatorName
                    binding.opponentName.text = challenge.opponentName.ifEmpty {
                        getString(R.string.challenge_open)
                    }

                    // Category and difficulty
                    binding.categoryText.text = challenge.categoryDisplay
                    binding.difficultyText.text = challenge.difficulty.displayName

                    // Status badge
                    val (statusText, statusBg) = when (challenge.status) {
                        ChallengeStatus.ACTIVE -> getString(R.string.status_active) to R.drawable.bg_badge_active
                        ChallengeStatus.COMPLETED -> getString(R.string.status_completed) to R.drawable.bg_badge_won
                        ChallengeStatus.PENDING -> getString(R.string.status_pending) to R.drawable.bg_badge_active
                        ChallengeStatus.CANCELLED -> getString(R.string.status_cancelled) to R.drawable.bg_badge_lost
                    }
                    binding.statusBadge.text = statusText
                    binding.statusBadge.setBackgroundResource(statusBg)

                    // Deadline
                    challenge.deadline?.let { deadline ->
                        val date = deadline.toDate()
                        val dateFormat = android.text.format.DateFormat.getMediumDateFormat(context)
                        binding.deadlineText.text = dateFormat.format(date)
                        binding.deadlineContainer.isVisible = true
                    } ?: run {
                        binding.deadlineContainer.isVisible = false
                    }

                    // Action buttons visibility
                    binding.completeButton.isVisible = state.canComplete
                    binding.acceptButton.isVisible = state.canAccept
                    binding.declineButton.isVisible = state.canAccept

                    // Show pending actions container
                    binding.pendingActionsContainer.isVisible = state.canAccept
                }

                // Handle success
                if (state.actionSuccess) {
                    Toast.makeText(context, R.string.challenge_action_success, Toast.LENGTH_SHORT).show()
                    viewModel.clearActionSuccess()
                }

                // Handle error
                state.error?.let { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
