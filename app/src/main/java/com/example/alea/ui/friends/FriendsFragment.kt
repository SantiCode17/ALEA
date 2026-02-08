package com.example.alea.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentFriendsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupClickListeners()
        observeState()
    }

    private fun setupRecyclerView() {
        friendsAdapter = FriendsAdapter(
            onFriendClick = { friendship ->
                // Navigate to chat
                val action = FriendsFragmentDirections.actionFriendsToChat(
                    friendId = friendship.friendId,
                    friendName = friendship.friendName
                )
                findNavController().navigate(action)
            },
            onMessageClick = { friendship ->
                // Navigate to chat
                val action = FriendsFragmentDirections.actionFriendsToChat(
                    friendId = friendship.friendId,
                    friendName = friendship.friendName
                )
                findNavController().navigate(action)
            }
        )
        binding.friendsRecyclerView.adapter = friendsAdapter
    }

    private fun setupSearch() {
        binding.searchEditText.doAfterTextChanged { text ->
            viewModel.searchUsers(text.toString())
        }
    }

    private fun setupClickListeners() {
        binding.addFriendButton.setOnClickListener {
            findNavController().navigate(R.id.action_friends_to_addFriend)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                friendsAdapter.submitList(state.friends)
                binding.emptyState.isVisible = state.friends.isEmpty() && !state.isLoading
                binding.friendsRecyclerView.isVisible = state.friends.isNotEmpty()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
