package com.example.alea.ui.challenge

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alea.R
import com.example.alea.databinding.FragmentVictoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VictoryFragment : Fragment() {

    private var _binding: FragmentVictoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVictoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set demo reward values
        binding.coinsAmount.text = "+250"
        binding.xpAmount.text = "+500 XP"

        binding.goHomeButton.setOnClickListener {
            findNavController().navigate(
                R.id.homeFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build()
            )
        }

        binding.shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.victory_share_text))
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.victory_share)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
