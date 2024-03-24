package com.kong.transmart.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.kong.transmart.MainActivity
import com.kong.transmart.R
import java.util.concurrent.atomic.AtomicInteger

object NotificationUtils {
    private val TAG = NotificationUtils::class.simpleName
    private const val CHANNEL_ID_LOW_PRICE_ALERT = "low_price_alert"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    fun createNotificationChannels(context: Context) {
        createNotificationChannel(context, CHANNEL_ID_LOW_PRICE_ALERT, "Low Price Alert",
            NotificationManager.IMPORTANCE_LOW, "Send a notification in the event of identifying the lowest price within a week.")
    }

    private fun createNotificationChannel(context: Context, channelId: String, channelName: String, importance: Int, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(channelId, channelName, importance)
            mChannel.description = description
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun sendNotification(context: Context, title: String, content: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, CHANNEL_ID_LOW_PRICE_ALERT)
            .setSmallIcon(R.drawable.transmart_notification_large)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, R.color.dark_gray))

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define.
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(TAG, "No permission to post notification")
                return
            }
            notify(NotificationID.id, builder.build())
        }
    }

    object NotificationID {
        val id: Int
            get() = AtomicInteger(0).incrementAndGet()
    }
}