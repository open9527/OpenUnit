package com.open.permission

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class PermissionManager(private val activity: FragmentActivity) {
    private var callback: PermissionsCallback? = null
    private var permissions: MutableList<String> = arrayListOf()

    private val launcher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { ps ->
            Log.d("PermissionManager", "Permissions: ${ps.entries}")
            val denyPermissions: MutableList<String> = arrayListOf()
            var isGranted = true
            ps.entries.forEach {
                if (!it.value) {
                    denyPermissions.add(it.key)
                }
                if (it.key in permissions && !it.value) {
                    isGranted = false
                }
            }
            if (!isGranted) {
                callback?.deny()
                callback?.denyPermissions(denyPermissions)

            } else {
                callback?.allow()
            }
        }

    fun requestPermissions(
        permissions: Array<String>,
        callback: PermissionsCallback
    ) {
        this.permissions.clear()
        this.permissions.addAll(permissions)

        this.callback = callback
        if (hasPermissions(activity)) {
            this.callback?.allow()
        } else {
            launcher.launch(permissions)

        }
    }

    private fun hasPermissions(context: Context) = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    fun checkPermissions(context: Context, arrays: Array<String>) = arrays.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


    fun setting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }

    fun locationSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        activity.startActivity(intent)
    }
}