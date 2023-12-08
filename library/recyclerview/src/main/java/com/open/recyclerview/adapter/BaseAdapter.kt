package com.open.recyclerview.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.open.recyclerview.animations.ItemAnimation

open class BaseAdapter<T : BaseCell>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val itemAnimation: ItemAnimation? = null
) :
    ListAdapter<T, BaseViewHolder>(diffCallback), LifecycleEventObserver {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return BaseViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position).bindViewHolder(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].getItemType()
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        addAnimation(holder)
    }


    fun addLifecycleOwner(owner: LifecycleOwner?) {
        owner?.lifecycle?.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        Log.d("BaseAdapter", "onStateChanged === $event")
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
            }

            Lifecycle.Event.ON_PAUSE -> {
            }

            Lifecycle.Event.ON_DESTROY -> {
            }

            else -> {

            }
        }
    }


    private fun addAnimation(holder: BaseViewHolder) {
        itemAnimation?.let {
            if (it.itemAnimEnabled) {
                if (!it.itemAnimFirstOnly || holder.layoutPosition > it.itemAnimStartPosition) {
                    startAnimation(holder, it)
                    it.itemAnimStartPosition = holder.layoutPosition
                }
            }
        }
    }

    protected open fun startAnimation(holder: BaseViewHolder, item: ItemAnimation) {
        item.itemAnimation?.let {
            for (anim in it.getAnimators(holder.itemView)) {
                anim.setDuration(item.itemAnimDuration).start()
                anim.interpolator = item.itemAnimInterpolator
            }
        }
    }
}


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
