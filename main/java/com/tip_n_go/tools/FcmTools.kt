package com.tip_n_go.tools

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.tip_n_go.App
import com.tip_n_go.R
import com.tip_n_go.ui.activity.MainActivity

object FcmTools {

    fun showNotification(message: RemoteMessage) {
        val intent = Intent(App.application.applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                App.application.applicationContext,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channelId = "channel_id"
        val title = message.data["Title"]
        val text = message.data["Body"]
        val builder = NotificationCompat.Builder(
            App.application.applicationContext, channelId
        )
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setSound(sound)
            .setContentIntent(pendingIntent)
        val manager =
            App.application.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        createNotificationChannel(manager, channelId)
        manager.notify(channelId, 0, builder.build())
    }

    private fun createNotificationChannel(manager: NotificationManager, channelId: String?) {
        val channelDescription = "Default Channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var notificationChannel = manager.getNotificationChannel(channelId)
            if (notificationChannel == null) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                notificationChannel = NotificationChannel(channelId, channelDescription, importance)
                notificationChannel.enableVibration(true)
                notificationChannel.setShowBadge(true)
                manager.createNotificationChannel(notificationChannel)
            }
        }
    }
}
