package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.alea.databinding.StepChooseOpponentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StepChooseOpponentFragment : Fragment() {

    private var _binding: StepChooseOpponentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateChallengeViewModel by viewModels({ requireParentFragment() })
    private lateinit var adapter: FriendSelectableAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StepChooseOpponentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = FriendSelectableAdapter { friend ->
            viewModel.selectOpponent(friend)
        }
        binding.friendsRecyclerView.adapter = adapter
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.friends)
                adapter.selectedId = state.selectedOpponent?.friendId
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
