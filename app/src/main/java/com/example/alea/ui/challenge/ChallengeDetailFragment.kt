package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import com.example.alea.data.model.ChallengeStatus
import com.example.alea.databinding.FragmentChallengeDetailBinding
import com.example.alea.ui.adapters.FriendAdapter

class ChallengeDetailFragment : Fragment() {

    private var _binding: FragmentChallengeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChallengeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.challengeDetailBack.setOnClickListener { findNavController().popBackStack() }

        // Show first active challenge as detail
        val challenge = MockDataProvider.challenges.firstOrNull { it.status == ChallengeStatus.ACTIVE }
            ?: MockDataProvider.challenges.first()

        binding.challengeDetailName.text = challenge.title
        binding.challengeDetailDesc.text = challenge.description.ifEmpty { "Sin descripción adicional" }
        binding.challengeDetailBet.text = "${challenge.bet} ★"
        binding.challengeDetailTime.text = challenge.deadline.ifEmpty { "Sin límite" }

        when (challenge.status) {
            ChallengeStatus.ACTIVE -> {
                binding.challengeDetailStatus.text = "Activo"
                binding.challengeDetailStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_success))
            }
            ChallengeStatus.PENDING -> {
                binding.challengeDetailStatus.text = "Pendiente"
                binding.challengeDetailStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_coins))
            }
            ChallengeStatus.COMPLETED -> {
                binding.challengeDetailStatus.text = "Completado"
                binding.challengeDetailStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_primary_start))
            }
            ChallengeStatus.REJECTED -> {
                binding.challengeDetailStatus.text = "Rechazado"
                binding.challengeDetailStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.color_error))
            }
        }

        // Participants
        val participants = MockDataProvider.friends.take(3)
        binding.challengeDetailParticipantsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.challengeDetailParticipantsRv.adapter = FriendAdapter(participants, {}, {})

        binding.challengeDetailActionBtn.setOnClickListener {
            Toast.makeText(requireContext(), "¡Reto completado! 🎉", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
