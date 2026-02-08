package com.example.alea.ui.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alea.R
import com.example.alea.databinding.BottomSheetAddFriendBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddFriendBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddFriendBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var suggestionsAdapter: UserSuggestionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddFriendBinding.inflate(inflater, container, false)
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
        suggestionsAdapter = UserSuggestionsAdapter { user ->
            viewModel.addFriend(user)
            Toast.makeText(context, R.string.add_friend_added, Toast.LENGTH_SHORT).show()
        }
        binding.suggestionsRecyclerView.adapter = suggestionsAdapter
    }

    private fun setupSearch() {
        binding.searchEditText.doAfterTextChanged { text ->
            viewModel.searchUsers(text.toString())
        }
    }

    private fun setupClickListeners() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                suggestionsAdapter.submitList(state.searchResults)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
