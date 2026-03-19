package com.arflix.tv.util

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.compose.runtime.compositionLocalOf

enum class DeviceType {
    TV,
    TABLET,
    PHONE;

    fun isTouchDevice(): Boolean = this == PHONE || this == TABLET

    fun isMobile(): Boolean = isTouchDevice()
}

val LocalDeviceType = compositionLocalOf { DeviceType.TV }

fun detectDeviceType(context: Context): DeviceType {
    val packageManager = context.packageManager

    if (packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK) ||
        packageManager.hasSystemFeature(PackageManager.FEATURE_TELEVISION)
    ) {
        return DeviceType.TV
    }

    val smallestWidthDp = context.resources.configuration.smallestScreenWidthDp
    if (smallestWidthDp >= 600) {
        return DeviceType.TABLET
    }

    return DeviceType.PHONE
}
