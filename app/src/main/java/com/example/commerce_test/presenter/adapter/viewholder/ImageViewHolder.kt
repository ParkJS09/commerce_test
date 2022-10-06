package com.example.commerce_test.presenter.adapter.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.commerce_test.data.models.Document
import com.example.commerce_test.databinding.ItemImageBinding

class ImageViewHolder(
    private val context: Context,
    private val binding: ItemImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(document: Document) {
        binding.item = document
    }
}