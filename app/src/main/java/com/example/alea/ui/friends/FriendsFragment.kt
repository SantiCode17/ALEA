package com.example.alea.ui.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import com.example.alea.data.model.Friend
import com.example.alea.databinding.FragmentFriendsBinding
import com.example.alea.ui.adapters.FriendAdapter

class FriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private var showingAll = true
    private var currentQuery = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.friendsRv.layoutManager = LinearLayoutManager(requireContext())
        loadFriends()

        binding.friendsTabAll.setOnClickListener {
            showingAll = true
            updateTabs()
            loadFriends()
        }

        binding.friendsTabRequests.setOnClickListener {
            showingAll = false
            updateTabs()
            loadFriends()
        }

        binding.friendsAddBtn.setOnClickListener {
            findNavController().navigate(R.id.action_friends_to_addFriend)
        }

        binding.friendsSearchBtn.setOnClickListener {
            val isVisible = binding.friendsSearchInput.visibility == View.VISIBLE
            binding.friendsSearchInput.visibility = if (isVisible) View.GONE else View.VISIBLE
            if (!isVisible) {
                binding.friendsSearchInput.requestFocus()
            } else {
                binding.friendsSearchInput.text?.clear()
                currentQuery = ""
                loadFriends()
            }
        }

        // Search filter
        binding.friendsSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s?.toString()?.trim() ?: ""
                loadFriends()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadFriends() {
        var list = if (showingAll) MockDataProvider.friends else MockDataProvider.friends.take(2)
        if (currentQuery.isNotEmpty()) {
            list = list.filter {
                it.name.contains(currentQuery, ignoreCase = true) ||
                it.username.contains(currentQuery, ignoreCase = true)
            }
        }
        binding.friendsRv.adapter = FriendAdapter(list,
            onChatClick = { findNavController().navigate(R.id.action_friends_to_chat) },
            onItemClick = { findNavController().navigate(R.id.action_friends_to_friendProfile) }
        )
    }

    private fun updateTabs() {
        val activeColor = ContextCompat.getColor(requireContext(), R.color.white)
        val inactiveColor = ContextCompat.getColor(requireContext(), R.color.color_text_secondary)

        if (showingAll) {
            binding.friendsTabAll.setBackgroundResource(R.drawable.shape_segmented_active)
            binding.friendsTabAll.setTextColor(activeColor)
            binding.friendsTabRequests.background = null
            binding.friendsTabRequests.setTextColor(inactiveColor)
        } else {
            binding.friendsTabRequests.setBackgroundResource(R.drawable.shape_segmented_active)
            binding.friendsTabRequests.setTextColor(activeColor)
            binding.friendsTabAll.background = null
            binding.friendsTabAll.setTextColor(inactiveColor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
