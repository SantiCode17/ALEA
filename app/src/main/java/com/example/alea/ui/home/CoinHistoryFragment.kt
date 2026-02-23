package com.example.alea.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alea.data.MockDataProvider
import com.example.alea.databinding.FragmentCoinsBinding
import com.example.alea.ui.adapters.CoinTransactionAdapter

class CoinHistoryFragment : Fragment() {

    private var _binding: FragmentCoinsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCoinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coinsBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.coinsTotal.text = MockDataProvider.currentUser.aleaCoins.toString()

        val adapter = CoinTransactionAdapter(MockDataProvider.coinHistory)
        binding.coinsTransactionsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.coinsTransactionsRv.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
