@file:Suppress("UNCHECKED_CAST")

package com.open.core.binding

import android.content.Context
import android.content.ContextWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.core.app.ComponentActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding


inline fun <reified T : ViewBinding> ComponentActivity.binding(owner: LifecycleOwner? = null) =
    binding(T::class.java, owner) {
        findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    }

inline fun <reified T : ViewBinding> Fragment.binding(owner: LifecycleOwner? = null) =
    binding(T::class.java, owner, this::requireView)

inline fun <reified T : ViewBinding> binding(
    owner: LifecycleOwner? = null,
    noinline viewFinder: () -> View
) = binding(T::class.java, owner, viewFinder)

inline fun <reified T : ViewBinding> ViewGroup.binding(owner: LifecycleOwner? = null) =
    inflate(T::class.java, owner, this)


val ViewGroup.resolveLifecycleOwner: LifecycleOwner
    get() {
        if (this is LifecycleOwner) return this
        var c: Context? = context
        do {
            if (c is ComponentActivity) return c
            c = if (c is ContextWrapper) c.baseContext else null
        } while (c != null)
        throw Exception("can not resolve LifecycleOwner")
    }

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal fun <T : ViewBinding> inflate(
    clazz: Class<T>,
    owner: LifecycleOwner? = null,
    view: ViewGroup
) = lazyBinding(owner) {
    val inflate = clazz.getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    inflate(null, LayoutInflater.from(view.context), view, true) as T
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal fun <T : ViewBinding> binding(
    clazz: Class<T>,
    owner: LifecycleOwner? = null,
    viewFinder: () -> View
) = lazyBinding(owner) {
    val view = viewFinder()
    val bind = clazz.getMethod("bind", View::class.java)
    val binding = bind(null, view) as T
    if (owner is Fragment && binding is ViewDataBinding) {
        binding.lifecycleOwner = owner.viewLifecycleOwner
    }
    binding
}

private fun <T : ViewBinding> lazyBinding(owner: LifecycleOwner?, initializer: () -> T): Lazy<T> {

    if (owner == null) {
        return lazy(initializer)
    }

    val lazyViewBinding = ResettableLazy {
        val binding = initializer()
        if (binding is ViewDataBinding && binding.lifecycleOwner == null) {
            binding.lifecycleOwner = owner
        }
        binding
    }

    val onDestroy = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY && lazyViewBinding.isInitialized()) {
            (lazyViewBinding.value as? ViewDataBinding)?.apply {
                unbind()
                lifecycleOwner = null
            }
            lazyViewBinding.reset()
        }
    }
    owner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
        if (event == Lifecycle.Event.ON_START) {
            val owner1 = (lazyViewBinding.value as? ViewDataBinding)?.lifecycleOwner
            if (owner1 != owner) {
                owner1?.lifecycle?.addObserver(onDestroy)
            }
        } else {
            onDestroy.onStateChanged(source, event)
        }
    })

    return lazyViewBinding
}

