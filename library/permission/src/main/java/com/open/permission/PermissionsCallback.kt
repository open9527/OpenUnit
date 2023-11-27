package com.open.permission

interface PermissionsCallback {
    fun allow() {}
    fun deny() {}
    fun denyPermissions(permissions: List<String>) {
    }
}