package com.example.alea.ui.profile

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
import com.example.alea.databinding.FragmentProfileBinding
import com.example.alea.ui.adapters.AchievementAdapter
import java.text.NumberFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = MockDataProvider.currentUser
        val nf = NumberFormat.getNumberInstance(Locale("es", "ES"))

        binding.profileName.text = user.name
        binding.profileHandle.text = user.username
        binding.profileLevel.text = getString(R.string.profile_level, user.level)
        binding.profileBio.text = "\"${user.bio}\""
        binding.profileMemberSince.text = getString(R.string.profile_member_since) + " · Enero 2024"

        binding.profileStatChallenges.text = "${user.totalChallenges}"
        binding.profileStatFriends.text = "${MockDataProvider.friends.size}"
        binding.profileStatCoins.text = nf.format(user.aleaCoins)

        binding.profileWeeklyPoints.text = "${user.weeklyPoints}"

        // Calculate win rate
        val completed = MockDataProvider.challenges.count { it.status == ChallengeStatus.COMPLETED }
        val total = MockDataProvider.challenges.size
        val winRate = if (total > 0) (completed * 100 / total) else 0
        binding.profileWinRate.text = "${winRate}%"

        // Achievements horizontal list
        binding.profileAchievementsRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.profileAchievementsRv.adapter = AchievementAdapter(MockDataProvider.unlockedAchievements.take(6), horizontalMode = true)

        binding.profileSeeAllAchievements.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_achievements)
        }

        // Edit profile → editProfile (was incorrectly going to coinHistory)
        binding.profileEditBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_editProfile)
        }

        // Quick actions
        binding.profileActionChallenges.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_challengesHistory)
        }

        binding.profileActionCoins.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_coinHistory)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
