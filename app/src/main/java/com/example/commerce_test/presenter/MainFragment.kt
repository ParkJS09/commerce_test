package com.example.commerce_test.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.GridLayout
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.commerce_test.R
import com.example.commerce_test.databinding.FragmentMainBinding
import com.example.commerce_test.presenter.adapter.FilterAdapter
import com.example.commerce_test.presenter.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val mainAdapter: MainAdapter by lazy {
        MainAdapter(mainViewModel)
    }
    private val filterAdapter: FilterAdapter by lazy {
        FilterAdapter(mainViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this@MainFragment
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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { state ->
                    when (state) {
                        is MainUiState.Empty -> {
                            if (state.isLoading) {
                                Toast.makeText(
                                    requireContext(),
                                    "데이터를 조회 중 입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        is MainUiState.Image -> {
                            mainAdapter.submitList(state.imageList)
                            filterAdapter.submitList(state.collectionList)
                        }
                    }

                    if (state.noticeMsg.isNotEmpty()) {
                        Toast.makeText(requireContext(), state.noticeMsg, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}