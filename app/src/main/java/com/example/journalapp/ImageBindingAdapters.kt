package com.example.journalapp.utils // Example package

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageBindingAdapters {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, url: String?) {
        if (url != null) {
            Glide.with(imageView.context)
                .load(url)
                .into(imageView)
        } else {
            // Optional: Load a placeholder image
            imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        }
    }
}
