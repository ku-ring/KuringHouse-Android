package com.yeonkyu.kuringhouse.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.yeonkyu.kuringhouse.R

@BindingAdapter("visibleIf")
fun View.visibleIf(value: Boolean) {
    visibility = when (value) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("audioEnableIf")
fun ImageView.audioEnableIf(value: Boolean?) {
    if (value == null) {
        visibility = View.GONE
        return
    }

    val resource = if (value) {
        R.drawable.ic_mic_on
    } else {
        R.drawable.ic_mic_off
    }
    setImageResource(resource)
}

@BindingAdapter("loadNormalImage")
fun ImageView.loadNormalImage(url: String) {
    if (url.isEmpty()) {
        Glide.with(this.context)
            .load(R.drawable.ic_person48)
            .into(this)
    } else {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }
}