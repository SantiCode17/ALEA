package com.example.alea.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import com.example.alea.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = MockDataProvider.currentUser

        binding.editProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.editProfileName.setText(user.name)
        binding.editProfileUsername.setText(user.username)
        binding.editProfileBio.setText(user.bio)

        binding.editProfileAvatar.setOnClickListener {
            Toast.makeText(requireContext(), "Galería no disponible en modo demo", Toast.LENGTH_SHORT).show()
        }

        binding.editProfileSave.setOnClickListener {
            Toast.makeText(requireContext(), "Perfil actualizado ✓", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
