package com.example.commerce_test.presenter.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(
    "itemImgUrl"
)
fun ImageView.setItemImage(
    itemImgUrl: String?,
) {
    itemImgUrl?.let { url ->
        Glide.with(context).clear(this)
        Glide.with(this).load(url).let { request ->
            request.placeholder(
                resources.getDrawable(
                    com.google.android.material.R.drawable.mtrl_ic_error,
                    null
                )
            ).error(
                resources.getDrawable(
                    com.google.android.material.R.drawable.ic_mtrl_chip_close_circle,
                    null
                )
            ).fallback(
                    resources.getDrawable(
                        com.google.android.material.R.drawable.ic_mtrl_chip_close_circle,
                        null
                    )
            ).into(this)
        }
    }
}