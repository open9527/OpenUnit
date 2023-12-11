package com.open.core

import android.content.pm.PackageManager

object AppUtils {

    fun getAppPackageName(): String = ContextHolder.get().packageName


    fun getAppName(): String? =getAppName(getAppPackageName())

    private fun getAppName(packageName: String?): String? {
        return if (packageName?.isEmpty() == true) "" else try {
            val pm: PackageManager = ContextHolder.get().packageManager
            val pi = pm.getPackageInfo(packageName!!, 0)
            pi?.applicationInfo?.loadLabel(pm)?.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    fun getAppVersionName(): String? {
        return getAppVersionName(getAppPackageName())
    }


    private fun getAppVersionName(packageName: String?): String? {
        return if (packageName?.isEmpty() == true) "" else try {
            val pm: PackageManager = ContextHolder.get().packageManager
            val pi = pm.getPackageInfo(packageName!!, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    fun getAppVersionCode(): Int {
        return getAppVersionCode(getAppPackageName())
    }

    private fun getAppVersionCode(packageName: String?): Int {
        return if (packageName?.isEmpty() == true) -1 else try {
            val pm: PackageManager = ContextHolder.get().packageManager
            val pi = pm.getPackageInfo(packageName!!, 0)
            pi?.versionCode ?: -1
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            -1
        }
    }
}