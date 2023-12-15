package com.open.pkg.binding.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.open.core.SizeUtils


object ImageViewLoadBindingAdapter {
    @BindingAdapter(
        value = [
            "bindImageViewLoadUrl",
            "bindImageViewLoadWidth",
            "bindImageViewLoadHeight",
            "bindImageViewLoadPlaceholderRes",
            "bindImageViewLoadPlaceholderDrawable",
            "bindImageViewLoadCircle",
            "bindImageViewLoadRound",
            "bindImageViewLoadRoundRadius",
            "bindImageViewLoadTransformation",
        ], requireAll = false
    )
    @JvmStatic
    fun setBindingImageViewLoad(
        imageView: ImageView?,
        data: Any?,
        width: Int?,
        height: Int?,
        @DrawableRes placeholderRes: Int?,
        placeholderDrawable: Drawable?,
        hasCircle: Boolean,
        hasRound: Boolean,
        roundRadius: Float,
        transformation: Transformation?,
    ) {
        imageView?.load(data) {
            if (width != null && height != null) {
                size(SizeUtils.dp2px(imageView.context,width.toFloat()).toInt(), SizeUtils.dp2px(imageView.context,height.toFloat()).toInt())
            }
            placeholderRes?.let {
                placeholder(it)
                fallback(it)
                error(it)
            }
            placeholderDrawable?.let {
                placeholder(it)
                fallback(it)
                error(it)
            }
            if (hasCircle) {
                transformations(CircleCropTransformation())
            }
            if (hasRound) {
                transformations(RoundedCornersTransformation(SizeUtils.dp2px(imageView.context,roundRadius)))
            }

            if (transformation != null) {
                transformations(transformation)
            }
        }
    }
}