package com.example.alea.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import com.example.alea.data.model.Notification
import com.example.alea.databinding.FragmentNotificationsBinding
import com.example.alea.ui.adapters.NotificationAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotificationAdapter
    private val notificationList = mutableListOf<Notification>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationList.clear()
        notificationList.addAll(MockDataProvider.notifications)

        adapter = NotificationAdapter(
            items = notificationList,
            onItemClick = { notification -> showNotificationDetail(notification) },
            onDeleteClick = { position -> adapter.removeItem(position) }
        )

        binding.notificationsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRv.adapter = adapter

        // Back button
        binding.notificationsBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Mark all read
        binding.notificationsMarkAll.setOnClickListener {
            adapter.markAllRead()
            Toast.makeText(requireContext(), "Todas marcadas como leídas ✓", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotificationDetail(notification: Notification) {
        val ctx = requireContext()
        val container = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 40, 60, 20)
        }

        val sender = TextView(ctx).apply {
            text = if (notification.senderName.isNotEmpty()) notification.senderName else "ALEA"
            setTextColor(resources.getColor(R.color.white, null))
            textSize = 18f
            typeface = resources.getFont(R.font.poppins_bold)
        }
        container.addView(sender)

        val time = TextView(ctx).apply {
            text = notification.time
            setTextColor(resources.getColor(R.color.color_text_hint, null))
            textSize = 12f
            typeface = resources.getFont(R.font.poppins)
            setPadding(0, 4, 0, 20)
        }
        container.addView(time)

        val message = TextView(ctx).apply {
            text = notification.text
            setTextColor(resources.getColor(R.color.color_text_secondary, null))
            textSize = 15f
            typeface = resources.getFont(R.font.poppins)
            setLineSpacing(8f, 1f)
        }
        container.addView(message)

        val dialog = MaterialAlertDialogBuilder(ctx, R.style.Theme_Alea_Dialog)
            .setView(container)
            .setPositiveButton("Cerrar", null)
            .create()

        dialog.show()
        // Style: orange button, proper margins
        dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE)?.apply {
            setTextColor(resources.getColor(R.color.color_primary_start, null))
            typeface = resources.getFont(R.font.poppins_semibold)
            isAllCaps = false
        }
        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.shape_dialog_bg)
            val params = attributes
            params.width = (resources.displayMetrics.widthPixels * 0.88).toInt()
            attributes = params
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
