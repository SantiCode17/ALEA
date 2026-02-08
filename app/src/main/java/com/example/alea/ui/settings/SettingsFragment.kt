package com.example.alea.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupColorPicker()
        setupClickListeners()
        observeState()
    }

    private fun setupColorPicker() {
        binding.colorPicker.onColorSelected = { color ->
            viewModel.setThemeColor(color)
            Toast.makeText(requireContext(), "Theme color updated!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }

        binding.changePasswordButton.setOnClickListener {
            Toast.makeText(requireContext(), "Change password coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.privacyButton.setOnClickListener {
            Toast.makeText(requireContext(), "Privacy settings coming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.termsButton.setOnClickListener {
            Toast.makeText(requireContext(), "Terms & conditions coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                binding.emailValue.text = state.email.ifEmpty { "player@alea.com" }

                // Set the selected color in the picker
                if (state.themeColor != 0) {
                    binding.colorPicker.setSelectedColor(state.themeColor)
                }

                if (state.isLoggedOut) {
                    findNavController().navigate(R.id.action_settings_to_login)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
