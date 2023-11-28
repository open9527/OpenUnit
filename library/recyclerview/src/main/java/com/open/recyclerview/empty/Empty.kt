package com.open.recyclerview.empty

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.setEmptyView(owner: LifecycleOwner, emptyView: View) =
    observeDataEmpty(owner) { (View.VISIBLE == emptyView.visibility) }

fun RecyclerView.observeDataEmpty(owner: LifecycleOwner, block: (Boolean) -> Unit) =
    owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        private var observer: RecyclerView.AdapterDataObserver? = null

        override fun onCreate(owner: LifecycleOwner) {
            if (observer == null) {
                val adapter = checkNotNull(adapter) {
                    "RecyclerView needs to set up the adapter before setting up an empty view."
                }
                observer = AdapterDataEmptyObserver(adapter, block)
                adapter.registerAdapterDataObserver(observer!!)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            observer?.let {
                adapter?.unregisterAdapterDataObserver(it)
                observer = null
            }
        }
    })

class AdapterDataEmptyObserver(
    private val adapter: RecyclerView.Adapter<*>,
    private val checkEmpty: (Boolean) -> Unit
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() = checkEmpty(isDataEmpty)

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = checkEmpty(isDataEmpty)

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = checkEmpty(isDataEmpty)

    private val isDataEmpty get() = adapter.itemCount == 0
}