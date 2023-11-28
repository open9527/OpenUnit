package com.open.recyclerview.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

inline fun <reified T : BaseCell> diffCallback(): DiffUtil.ItemCallback<T> =
    object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }
    }