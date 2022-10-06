package com.example.commerce_test.presenter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.commerce_test.R
import com.example.commerce_test.databinding.FragmentMainBinding
import com.example.commerce_test.presenter.adapter.FilterAdapter
import com.example.commerce_test.presenter.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }
    private val filterAdapter: FilterAdapter by lazy {
        FilterAdapter(mainViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate<FragmentMainBinding?>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        ).apply {
            viewModel = mainViewModel
            lifecycleOwner = this@MainFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
        binding.rvFilter.apply {
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { state ->
                    if (state.isLoading) {
                        Toast.makeText(
                            requireContext(),
                            R.string.loading_msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if (state.noticeMsg.isNotEmpty()) {
                        showToast(state.noticeMsg)
                    }

                    when (state) {
                        is MainUiState.Empty -> {
                            binding.tvEmptyMsg.visibility = View.VISIBLE
                        }
                        is MainUiState.Image -> {
                            if (binding.tvEmptyMsg.isVisible) {
                                binding.tvEmptyMsg.visibility = View.GONE
                            }
                            mainAdapter.submitList(state.imageList)
                            filterAdapter.submitList(state.collectionList)
                        }
                    }
                }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}