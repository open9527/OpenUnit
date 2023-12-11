package com.open.core

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

object ActivityLifecycle {
    private val IGNORED_FRAGMENTS = listOf("SupportRequestManagerFragment")
    fun initialize(boolean: Boolean = false, application: Application) {

        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                LogUtils.d(
                    boolean,
                    "ActivityLifecycle  onActivityCreated: ${activity.javaClass.simpleName}"
                )
                if (activity is FragmentActivity) {
                    registerFragmentLifecycle(boolean, activity.supportFragmentManager)
                }
            }

            override fun onActivityStarted(activity: Activity) {
//                LogUtils.d(
//                    boolean,
//                    "ActivityLifecycle  onActivityStarted: ${activity.javaClass.simpleName}"
//                )
            }

            override fun onActivityResumed(activity: Activity) {
//                LogUtils.d(
//                    boolean,
//                    "ActivityLifecycle  onActivityResumed: ${activity.javaClass.simpleName}"
//                )
            }

            override fun onActivityPaused(activity: Activity) {
//                LogUtils.d(
//                    boolean,
//                    "ActivityLifecycle  onActivityPaused: ${activity.javaClass.simpleName}"
//                )
            }

            override fun onActivityStopped(activity: Activity) {
//                LogUtils.d(
//                    boolean,
//                    "ActivityLifecycle  onActivityStopped: ${activity.javaClass.simpleName}"
//                )
            }

            override fun onActivitySaveInstanceState(
                activity: Activity,
                savedInstanceState: Bundle
            ) {
//                LogUtils.d(
//                    boolean,
//                    "ActivityLifecycle  onActivitySaveInstanceState: ${activity.javaClass.simpleName}"
//                )
            }

            override fun onActivityDestroyed(activity: Activity) {
                LogUtils.d(
                    boolean,
                    "ActivityLifecycle  onActivityDestroyed: ${activity.javaClass.simpleName}"
                )
            }

        })
    }

    private fun registerFragmentLifecycle(boolean: Boolean, fragmentManager: FragmentManager) {
        fun ignoredFragments(fragment: Fragment): Boolean {
            return IGNORED_FRAGMENTS.contains(fragment.javaClass.simpleName)
        }

        fragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentCreated(
                fm: FragmentManager,
                fragment: Fragment,
                savedInstanceState: Bundle?
            ) {
                super.onFragmentCreated(fm, fragment, savedInstanceState)
                if (!ignoredFragments(fragment)) {
                    LogUtils.d(
                        boolean,
                        "FragmentLifecycle onFragmentCreated:${fragment.javaClass.simpleName}"
                    )
                }
            }

            override fun onFragmentDestroyed(fragmentManager: FragmentManager, fragment: Fragment) {
                super.onFragmentDestroyed(fragmentManager, fragment)
                if (!ignoredFragments(fragment)) {
                    LogUtils.d(
                        boolean,
                        "FragmentLifecycle onFragmentDestroyed:${fragment.javaClass.simpleName}"
                    )
                }
            }

        }, true)
    }

}