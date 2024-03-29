package com.udacity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import timber.log.Timber

private const val NOTIFICATION_ID = 0
const val DOWNLOAD_ID = "DOWNLOAD_ID"


fun NotificationManager.cancelNotification(){
    cancelAll()
}

fun NotificationManager.sendNotification(
    messageBody: String,
    downloadId: Long,
    channelId: String,
    context: Context
) {
    ifSupportsOreo {
        Timber.d("starting NotificationManager.sendNotification with id: %d", downloadId)

        val bundle = Bundle()
        bundle.putLong(DOWNLOAD_ID, downloadId)
        bundle.putString("TEST", "test")

        val contentIntent = Intent(context, DetailActivity::class.java)
        contentIntent.putExtras(bundle)

        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val bmp = AppCompatResources
            .getDrawable(context, R.mipmap.ic_launcher)?.toBitmap()

        val style = NotificationCompat.BigPictureStyle()
            .bigPicture(bmp)
            .setBigContentTitle(context.getString(R.string.notification_title))

        val builder = NotificationCompat.Builder(
            context,
            channelId
        ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setPriority(Notification.PRIORITY_MAX)
            .setChannelId(channelId)
            .setAutoCancel(true)
            .addExtras(bundle)
            .setExtras(bundle)
            .setStyle(style)
            .addAction(
                R.drawable.ic_assistant_black_24dp,
                context.getString(R.string.notification_button),
                contentPendingIntent
            )
        Timber.d("sending notification")
        notify(NOTIFICATION_ID, builder.build())
    }
}

fun NotificationManager.createChannel(channelId: String, channelName: String) {
    ifSupportsOreo {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = "Download complete!"
        }

        Timber.d("Creating NotifcationChannel")
        createNotificationChannel(notificationChannel)
    }
}

fun ifSupportsOreo(f: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        f()
    }else{
        Timber.w("Function execution not supported due to low API level <33")
    }
}