package com.example.commerce_test.presenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.commerce_test.R
import com.example.commerce_test.data.models.Document
import com.example.commerce_test.databinding.ItemImageBinding
import com.example.commerce_test.presenter.MainViewModel

class MainAdapter(
    private val mainViewModel: MainViewModel
) : ListAdapter<Document, RecyclerView.ViewHolder>(SummonerMatchDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            parent.context,
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ImageViewHolder).bind(getItem(position) as Document)
    }


    inner class ImageViewHolder(
        private val context: Context,
        private val binding: ItemImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document) {
            binding.item = document
        }
    }
}

private class SummonerMatchDiffCallback : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.doc_url == newItem.doc_url
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}