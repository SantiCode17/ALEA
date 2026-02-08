package com.example.alea.ui.challenge

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CreateChallengeStepsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StepChallengeDetailsFragment()
            1 -> StepChooseOpponentFragment()
            2 -> StepChallengeFinalFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
