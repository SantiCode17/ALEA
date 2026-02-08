package com.example.alea.ui.notifications

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
import com.example.alea.data.model.NotificationType
import com.example.alea.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeState()
    }

    private fun setupRecyclerView() {
        notificationsAdapter = NotificationsAdapter(
            onNotificationClick = { notification ->
                // Mark as read
                viewModel.markAsRead(notification.id)

                // Navigate based on notification type
                when (notification.type) {
                    NotificationType.CHALLENGE_RECEIVED,
                    NotificationType.CHALLENGE_ACCEPTED,
                    NotificationType.CHALLENGE_COMPLETED,
                    NotificationType.CHALLENGE_WON,
                    NotificationType.CHALLENGE_LOST -> {
                        if (notification.actionId.isNotEmpty()) {
                            val action = NotificationsFragmentDirections
                                .actionNotificationsToChallengeDetail(notification.actionId)
                            findNavController().navigate(action)
                        }
                    }
                    NotificationType.FRIEND_REQUEST,
                    NotificationType.FRIEND_ACCEPTED -> {
                        findNavController().navigate(R.id.friendsFragment)
                    }
                    else -> { /* No navigation */ }
                }
            },
            onNotificationDismiss = { notification ->
                viewModel.deleteNotification(notification.id)
            }
        )
        binding.notificationsRecyclerView.adapter = notificationsAdapter
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.markAllReadButton.setOnClickListener {
            viewModel.markAllAsRead()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Update list
                notificationsAdapter.submitList(state.notifications)

                // Show/hide empty state
                binding.emptyStateContainer.isVisible =
                    state.notifications.isEmpty() && !state.isLoading
                binding.notificationsRecyclerView.isVisible =
                    state.notifications.isNotEmpty()

                // Show/hide loading
                binding.loadingIndicator.isVisible = state.isLoading

                // Update mark all read button visibility
                binding.markAllReadButton.isVisible = state.unreadCount > 0
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
