package com.example.alea.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.R
import com.example.alea.data.MockDataProvider
import com.example.alea.data.model.ChallengeStatus
import com.example.alea.databinding.FragmentHomeBinding
import com.example.alea.ui.adapters.ActivityAdapter
import com.example.alea.ui.adapters.ChallengeCardAdapter
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = MockDataProvider.currentUser

        // Header
        binding.homeUsername.text = user.name.split(" ").first()

        // Coins
        val nf = NumberFormat.getNumberInstance(Locale("es", "ES"))
        binding.homeCoinsAmount.text = "★ ${nf.format(user.aleaCoins)}"

        // Navigate to coin history
        binding.homeCoinBanner.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_coinHistory)
        }

        // Challenges RecyclerView (horizontal)
        val activeChallenges = MockDataProvider.challenges.filter {
            it.status == ChallengeStatus.ACTIVE || it.status == ChallengeStatus.PENDING
        }
        binding.homeChallengesRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.homeChallengesRv.adapter = ChallengeCardAdapter(activeChallenges) { challenge ->
            findNavController().navigate(R.id.action_home_to_challengeDetail)
        }

        binding.homeSeeAllChallenges.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_challengesHistory)
        }

        // Quick Stats
        val wins = MockDataProvider.challenges.count { it.status == ChallengeStatus.COMPLETED }
        binding.homeStatWins.text = "$wins"
        binding.homeStatStreak.text = "3 🔥"
        binding.homeStatRank.text = "#4"

        // Recent Activity
        binding.homeActivityRv.layoutManager = LinearLayoutManager(requireContext())
        binding.homeActivityRv.adapter = ActivityAdapter(MockDataProvider.notifications.take(4))

        // Notifications
        binding.homeNotifications.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_notifications)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
