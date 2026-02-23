package com.example.alea.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.data.MockDataProvider
import com.example.alea.databinding.FragmentNotificationsBinding
import com.example.alea.ui.adapters.NotificationAdapter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRv.adapter = NotificationAdapter(MockDataProvider.notifications)

        binding.notificationsMarkAll.setOnClickListener {
            Toast.makeText(requireContext(), "Todas marcadas como leídas ✓", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
