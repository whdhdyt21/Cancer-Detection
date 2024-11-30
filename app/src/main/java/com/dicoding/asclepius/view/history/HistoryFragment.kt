package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.adapter.ListHistoryAdapter
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.view.ViewModelFactory
import com.dicoding.asclepius.view.result.ClassificationViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var classificationViewModel: ClassificationViewModel
    private lateinit var historyAdapter: ListHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        classificationViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory.getInstance(requireActivity().application)
        )[ClassificationViewModel::class.java]

        setupRecyclerView()
        observeData()
        return binding.root
    }

    private fun setupRecyclerView() {
        historyAdapter = ListHistoryAdapter { entity ->
            classificationViewModel.delete(entity)
        }
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun observeData() {
        classificationViewModel.getAllClassification().observe(viewLifecycleOwner) { historyList ->
            binding.nothingData.isVisible = historyList.isEmpty()
            historyAdapter.submitList(historyList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
