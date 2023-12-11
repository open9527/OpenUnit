package com.open.unit.utils

import android.content.ComponentName
import android.content.pm.PackageManager
import com.open.core.ContextHolder

object LauncherIcon {

    fun switchIcon(enabledCls: Class<*>, disabledCls: Class<*>) {
        val packageManager = ContextHolder.get().packageManager
        packageManager.setComponentEnabledSetting(
            ComponentName(
                ContextHolder.get(),
                enabledCls.name
            ),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        packageManager.setComponentEnabledSetting(
            ComponentName(
                ContextHolder.get(),
                disabledCls.name
            ),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}