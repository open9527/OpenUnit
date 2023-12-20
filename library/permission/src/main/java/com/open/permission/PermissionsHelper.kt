package com.open.permission

import android.Manifest
import android.os.Build

/**
 * 申请相机权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestCamera(callback: PermissionsCallback) {
    val permissions = arrayOf(Manifest.permission.CAMERA)
    requestPermissions(permissions, callback)
}


/**
 * 申请位置权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestLocation(callback: PermissionsCallback) {
    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    requestPermissions(permissions, callback)
}


/**
 * 申请录音权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestRecordAudio(callback: PermissionsCallback) {
    val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
    requestPermissions(permissions, callback)
}

/**
 * 申请传感器权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestSensors(callback: PermissionsCallback) {
    val permissions = arrayOf(Manifest.permission.BODY_SENSORS)
    requestPermissions(permissions, callback)
}


/**
 * 申请存储空间权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestStorage(callback: PermissionsCallback) {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
        )
    else
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    requestPermissions(permissions, callback)
}


/**
 * 单独申请图片存储空间权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestMediaImages(callback: PermissionsCallback) {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    else
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    requestPermissions(permissions, callback)
}


/**
 * 单独申请视频存储空间权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestMediaVideo(callback: PermissionsCallback) {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(
            Manifest.permission.READ_MEDIA_VIDEO,
        )
    else
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    requestPermissions(permissions, callback)
}


/**
 *单独申请音频存储空间权限
 *
 * @param callback 回调
 */
fun PermissionManager.requestMediaAudio(callback: PermissionsCallback) {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
        )
    else
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    requestPermissions(permissions, callback)
}
