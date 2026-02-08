package com.example.alea.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alea.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var messagesAdapter: MessagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeState()
    }

    private fun setupRecyclerView() {
        messagesAdapter = MessagesAdapter(viewModel.uiState.value.currentUserId)
        binding.messagesRecyclerView.adapter = messagesAdapter
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.sendButton.setOnClickListener {
            val text = binding.messageInput.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendMessage(text)
                binding.messageInput.text?.clear()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.friendName.text = state.friendName
                messagesAdapter.submitList(state.messages) {
                    // Scroll to bottom when new messages arrive
                    if (state.messages.isNotEmpty()) {
                        binding.messagesRecyclerView.scrollToPosition(state.messages.size - 1)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
