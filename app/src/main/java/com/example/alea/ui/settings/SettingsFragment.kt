package com.example.alea.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentSettingsBinding
import com.example.alea.ui.auth.AuthActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure settings rows
        configureRow(binding.settingsEditProfile.root, R.drawable.ic_person, getString(R.string.settings_edit_profile))
        configureRow(binding.settingsPrivacy.root, R.drawable.ic_security, getString(R.string.settings_privacy))
        configureRow(binding.settingsNotificationsPref.root, R.drawable.ic_notifications, getString(R.string.settings_notifications))
        configureRow(binding.settingsTheme.root, R.drawable.ic_palette, getString(R.string.settings_theme))
        configureRow(binding.settingsLanguage.root, R.drawable.ic_star, getString(R.string.settings_language))
        configureRow(binding.settingsHelp.root, R.drawable.ic_chat, getString(R.string.settings_help))
        configureRow(binding.settingsTerms.root, R.drawable.ic_description, getString(R.string.settings_terms))
        configureRow(binding.settingsVersion.root, R.drawable.ic_settings, "Versión 1.0.0")

        // Click listeners
        binding.settingsEditProfile.root.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_editProfile)
        }
        binding.settingsPrivacy.root.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_security)
        }

        binding.settingsLogout.setOnClickListener {
            requireActivity().getSharedPreferences("alea_prefs", 0).edit()
                .putBoolean("is_logged_in", false)
                .putBoolean("is_first_launch", true)
                .apply()
            startActivity(Intent(requireContext(), com.example.alea.ui.onboarding.OnboardingActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun configureRow(row: View, iconRes: Int, title: String) {
        row.findViewById<ImageView>(R.id.settings_row_icon).setImageResource(iconRes)
        row.findViewById<TextView>(R.id.settings_row_title).text = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
