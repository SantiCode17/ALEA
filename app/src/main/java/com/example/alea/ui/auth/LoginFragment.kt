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
import com.example.alea.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeState()
    }

    private fun setupClickListeners() {
        binding.signInButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            viewModel.signIn(email, password)
        }

        binding.signUpLink.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
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
                        binding.signInButton.isEnabled = true
                    }
                    is AuthUiState.Loading -> {
                        binding.loadingProgress.isVisible = true
                        binding.signInButton.isEnabled = false
                        binding.signInButton.text = ""
                    }
                    is AuthUiState.Success -> {
                        findNavController().navigate(R.id.action_login_to_home)
                    }
                    is AuthUiState.Error -> {
                        binding.loadingProgress.isVisible = false
                        binding.signInButton.isEnabled = true
                        binding.signInButton.text = getString(R.string.login_sign_in)
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
