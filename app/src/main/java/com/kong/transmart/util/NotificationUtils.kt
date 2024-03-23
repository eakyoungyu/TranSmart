package com.kong.transmart.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

object NotificationUtils {
    fun sendNotification() {
        // Send a notification
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
}