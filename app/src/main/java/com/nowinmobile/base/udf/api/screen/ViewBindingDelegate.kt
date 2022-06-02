package com.nowinmobile.base.udf.api.screen

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/** Convenient extension to create new [ViewBinding] associated with the [FragmentActivity]. */
inline fun <reified T : ViewBinding> FragmentActivity.viewBinding(): ReadOnlyProperty<FragmentActivity, T> {
    return ViewBindingActivityDelegate(T::class.java, activity = this)
}

/** Convenient extension to create new [ViewBinding] associated with the [Fragment]. */
inline fun <reified T : ViewBinding> Fragment.viewBinding(): ReadOnlyProperty<Fragment, T> {
    return ViewBindingFragmentDelegate(T::class.java, fragment = this)
}

class ViewBindingActivityDelegate<T : ViewBinding>(
    private val viewBindingClass: Class<T>,
    private val activity: FragmentActivity,
) : ReadOnlyProperty<FragmentActivity, T> {
    private var viewBinding: T? = null

    override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
        val binding = viewBinding
        if (binding != null) return binding

        viewBinding = viewBindingClass.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, activity.layoutInflater) as T
        return viewBinding!!
    }
}

class ViewBindingFragmentDelegate<T : ViewBinding>(
    private val viewBindingClass: Class<T>,
    private val fragment: Fragment,
) : ReadOnlyProperty<Fragment, T> {
    private var viewBinding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> { lifecycleOwner ->
                val viewLifecycleOwner = lifecycleOwner ?: return@Observer

                viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        viewBinding = null
                    }
                })
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }


    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = viewBinding
        if (binding != null) return binding

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Illegal ViewBinding access when Fragment views are destroyed.")
        }

        viewBinding = viewBindingClass.getMethod("bind", View::class.java)
            .invoke(null, thisRef.requireView()) as T
        return viewBinding!!
    }
}
