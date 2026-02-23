package com.example.alea.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.data.MockDataProvider
import com.example.alea.data.model.Message
import com.example.alea.databinding.FragmentChatBinding
import com.example.alea.ui.adapters.MessageAdapter

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MessageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatName.text = "Nuria Rodríguez"
        binding.chatBack.setOnClickListener { findNavController().popBackStack() }

        val messages = MockDataProvider.chatWithNuria.toMutableList()
        adapter = MessageAdapter(messages)
        binding.chatMessagesRv.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        binding.chatMessagesRv.adapter = adapter

        binding.chatSendBtn.setOnClickListener {
            val text = binding.chatInput.text.toString().trim()
            if (text.isNotEmpty()) {
                adapter.addMessage(Message(true, text, "Ahora"))
                binding.chatInput.text?.clear()
                binding.chatMessagesRv.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
