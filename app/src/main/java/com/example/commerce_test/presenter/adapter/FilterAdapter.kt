package com.example.commerce_test.presenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.commerce_test.R
import com.example.commerce_test.databinding.ItemFilterBinding
import com.example.commerce_test.presenter.MainViewModel

class FilterAdapter(
    private val mainViewModel: MainViewModel
) : ListAdapter<String, RecyclerView.ViewHolder>(FilterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FilterViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_filter,
                parent,
                false
            ),
            mainViewModel
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FilterViewHolder).bind(getItem(position))
    }

    inner class FilterViewHolder(
        private val binding: ItemFilterBinding,
        private val mainViewModel: MainViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filter: String) {
            binding.viewModel = mainViewModel
            binding.item = filter
        }
    }
}

private class FilterDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}