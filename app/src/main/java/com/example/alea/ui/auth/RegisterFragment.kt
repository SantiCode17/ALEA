package com.example.alea.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.MainActivity
import com.example.alea.R
import com.example.alea.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnGoLogin.setOnClickListener { findNavController().popBackStack() }

        binding.btnRegister.setOnClickListener {
            val name = binding.inputName.text.toString().trim()
            val username = binding.inputUsername.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            // Validaciones
            if (name.isEmpty()) {
                binding.inputName.error = getString(R.string.register_error_empty_name)
                shakeView(binding.inputName)
                return@setOnClickListener
            }
            if (username.isEmpty()) {
                binding.inputUsername.error = getString(R.string.register_error_empty_user)
                shakeView(binding.inputUsername)
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.inputEmail.error = getString(R.string.register_error_empty_email)
                shakeView(binding.inputEmail)
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = getString(R.string.register_error_invalid_email)
                shakeView(binding.inputEmail)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.inputPassword.error = getString(R.string.register_error_empty_pass)
                shakeView(binding.inputPassword)
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.inputPassword.error = getString(R.string.register_error_pass_short)
                shakeView(binding.inputPassword)
                return@setOnClickListener
            }

            // Comprobar si el usuario ya existe
            val usersPrefs = requireActivity().getSharedPreferences("alea_users", Context.MODE_PRIVATE)
            if (usersPrefs.contains("user_$username")) {
                binding.inputUsername.error = getString(R.string.register_error_user_taken)
                shakeView(binding.inputUsername)
                Toast.makeText(requireContext(), getString(R.string.register_error_user_taken), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar usuario
            usersPrefs.edit()
                .putString("user_$username", password)
                .putString("email_$username", email)
                .putString("name_$username", name)
                .apply()

            // Marcar como logueado
            requireActivity().getSharedPreferences("alea_prefs", Context.MODE_PRIVATE).edit()
                .putBoolean("is_logged_in", true)
                .putString("logged_user", username)
                .apply()

            Toast.makeText(requireContext(), "Cuenta creada exitosamente ✅", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            requireActivity().finish()
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
