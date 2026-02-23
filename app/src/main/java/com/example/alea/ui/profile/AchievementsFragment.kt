package com.example.alea.ui.profile

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.data.MockDataProvider
import com.example.alea.databinding.FragmentAchievementsBinding
import com.example.alea.ui.adapters.AchievementAdapter

class AchievementsFragment : Fragment() {

    private var _binding: FragmentAchievementsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAchievementsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.achievementsBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val allAchievements = MockDataProvider.unlockedAchievements + MockDataProvider.lockedAchievements
        val unlocked = MockDataProvider.unlockedAchievements.size
        val total = allAchievements.size

        binding.achievementsProgress.text = "$unlocked / $total desbloqueados"
        binding.achievementsProgressBar.max = total
        binding.achievementsProgressBar.progress = unlocked

        val adapter = AchievementAdapter(allAchievements)
        val spanCount = 3
        binding.achievementsRv.layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.achievementsRv.adapter = adapter

        // Add spacing between grid items
        val spacing = (12 * resources.displayMetrics.density).toInt()
        binding.achievementsRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val position = parent.getChildAdapterPosition(view)
                val column = position % spanCount

                outRect.left = spacing - column * spacing / spanCount
                outRect.right = (column + 1) * spacing / spanCount
                outRect.bottom = spacing
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
