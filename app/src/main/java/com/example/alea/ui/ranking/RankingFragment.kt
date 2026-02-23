package com.example.alea.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.data.MockDataProvider
import com.example.alea.databinding.FragmentRankingBinding
import com.example.alea.ui.adapters.RankingAdapter

class RankingFragment : Fragment() {

    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRankingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rankingBack.setOnClickListener { findNavController().popBackStack() }

        val ranking = MockDataProvider.weeklyRanking

        // Podium top 3
        if (ranking.size >= 3) {
            binding.ranking1stName.text = ranking[0].name.split(" ").first()
            binding.ranking2ndName.text = ranking[1].name.split(" ").first()
            binding.ranking3rdName.text = ranking[2].name.split(" ").first()
        }

        // Rest of the ranking (4th onwards)
        val rest = if (ranking.size > 3) ranking.subList(3, ranking.size) else emptyList()
        binding.rankingRv.layoutManager = LinearLayoutManager(requireContext())
        binding.rankingRv.adapter = RankingAdapter(rest) {
            findNavController().navigate(com.example.alea.R.id.action_ranking_to_userDetail)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
