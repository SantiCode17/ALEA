package com.example.alea.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeState()
    }

    private fun setupClickListeners() {
        binding.signUpButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            viewModel.signUp(username, email, password, confirmPassword)
        }

        binding.signInLink.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }

        binding.googleButton.setOnClickListener {
            // TODO: Implement Google Sign-In
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is AuthUiState.Idle -> {
                        binding.loadingProgress.isVisible = false
                        binding.signUpButton.isEnabled = true
                    }
                    is AuthUiState.Loading -> {
                        binding.loadingProgress.isVisible = true
                        binding.signUpButton.isEnabled = false
                        binding.signUpButton.text = ""
                    }
                    is AuthUiState.Success -> {
                        findNavController().navigate(R.id.action_register_to_home)
                    }
                    is AuthUiState.Error -> {
                        binding.loadingProgress.isVisible = false
                        binding.signUpButton.isEnabled = true
                        binding.signUpButton.text = getString(R.string.register_sign_up)
                        Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG)
                            .setBackgroundTint(resources.getColor(R.color.alea_error, null))
                            .setTextColor(resources.getColor(R.color.white, null))
                            .show()
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
