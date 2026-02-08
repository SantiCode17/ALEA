package com.example.alea.ui.challenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.alea.R
import com.example.alea.data.model.ChallengeCategory
import com.example.alea.data.model.ChallengeDifficulty
import com.example.alea.databinding.StepChallengeDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepChallengeDetailsFragment : Fragment() {

    private var _binding: StepChallengeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateChallengeViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StepChallengeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextInputs()
        setupCategorySelection()
        setupDifficultySelection()
        setupStakeSlider()
    }

    private fun setupTextInputs() {
        binding.titleEditText.doAfterTextChanged { text ->
            viewModel.updateTitle(text.toString())
        }

        binding.descriptionEditText.doAfterTextChanged { text ->
            viewModel.updateDescription(text.toString())
        }
    }

    private fun setupCategorySelection() {
        binding.categoryChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener

            val category = when (checkedIds.first()) {
                R.id.chipFitness -> ChallengeCategory.FITNESS
                R.id.chipGaming -> ChallengeCategory.GAMING
                R.id.chipArt -> ChallengeCategory.ART
                R.id.chipCooking -> ChallengeCategory.COOKING
                R.id.chipStudy -> ChallengeCategory.STUDY
                R.id.chipMusic -> ChallengeCategory.MUSIC
                R.id.chipSports -> ChallengeCategory.SPORTS
                else -> ChallengeCategory.OTHER
            }
            viewModel.updateCategory(category)
        }
    }

    private fun setupDifficultySelection() {
        binding.difficultyChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener

            val difficulty = when (checkedIds.first()) {
                R.id.chipEasy -> ChallengeDifficulty.EASY
                R.id.chipMedium -> ChallengeDifficulty.MEDIUM
                R.id.chipHard -> ChallengeDifficulty.HARD
                R.id.chipExtreme -> ChallengeDifficulty.EXTREME
                else -> ChallengeDifficulty.MEDIUM
            }
            viewModel.updateDifficulty(difficulty)
        }
    }

    private fun setupStakeSlider() {
        // Update display when slider changes
        binding.stakeSlider.addOnChangeListener { _, value, _ ->
            binding.stakeAmount.text = getString(R.string.create_stake_format, value.toInt())
            viewModel.updateBetAmount(value.toLong())
        }

        // Initial value
        binding.stakeAmount.text = getString(R.string.create_stake_format, 100)

        // Quick stake buttons
        binding.stake100.setOnClickListener {
            binding.stakeSlider.value = 100f
        }
        binding.stake250.setOnClickListener {
            binding.stakeSlider.value = 250f
        }
        binding.stake500.setOnClickListener {
            binding.stakeSlider.value = 500f
        }
        binding.stake1000.setOnClickListener {
            binding.stakeSlider.value = 1000f
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
