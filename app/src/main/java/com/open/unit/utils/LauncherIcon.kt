package com.open.unit.utils

import android.content.ComponentName
import android.content.pm.PackageManager
import com.open.core.ContextHolder

object LauncherIcon {
    /**
     * 1.配置icon的activity 直接继承SplashActivity,
     * 2.配置icon的activity要把 enabled 属性设为false(否则出现多个icon),配置指定icon属性
     * 3.切换最佳时机是监听app切换到后台触发变更icon
     */
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