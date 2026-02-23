package com.example.alea.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.MainActivity
import com.example.alea.R
import com.example.alea.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            // Validaciones
            if (username.isEmpty()) {
                binding.inputUsername.error = getString(R.string.login_error_empty_user)
                shakeView(binding.inputUsername)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.inputPassword.error = getString(R.string.login_error_empty_pass)
                shakeView(binding.inputPassword)
                return@setOnClickListener
            }
            if (password.length < 4) {
                binding.inputPassword.error = getString(R.string.login_error_pass_short)
                shakeView(binding.inputPassword)
                return@setOnClickListener
            }

            // Comprobar que el usuario existe en SharedPreferences
            val usersPrefs = requireActivity().getSharedPreferences("alea_users", Context.MODE_PRIVATE)
            val savedPass = usersPrefs.getString("user_$username", null)

            if (savedPass == null) {
                binding.inputUsername.error = getString(R.string.login_error_not_registered)
                shakeView(binding.inputUsername)
                Toast.makeText(requireContext(), getString(R.string.login_error_not_registered), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (savedPass != password) {
                binding.inputPassword.error = getString(R.string.login_error_wrong_pass)
                shakeView(binding.inputPassword)
                Toast.makeText(requireContext(), getString(R.string.login_error_wrong_pass), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Login exitoso
            requireActivity().getSharedPreferences("alea_prefs", Context.MODE_PRIVATE).edit()
                .putBoolean("is_logged_in", true)
                .putString("logged_user", username)
                .apply()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().finish()
        }

        binding.btnGoRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.btnForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot)
        }
    }

    private fun shakeView(v: View) {
        v.animate().translationX(10f).setDuration(50)
            .withEndAction {
                v.animate().translationX(-10f).setDuration(50)
                    .withEndAction {
                        v.animate().translationX(0f).setDuration(50).start()
                    }.start()
            }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
