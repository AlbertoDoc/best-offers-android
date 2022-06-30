package com.bestoffers.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bestoffers.R
import com.bestoffers.repositories.room.entities.Product

class NotificationHelper(var product: Product, var context: Context) {

    fun show() {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel("com.bestoffers", "${product.name} caiu de preço!", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(context, "com.bestoffers")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                .setContentTitle("O preço caiu!")
                .setContentText("${product.name} caiu de preço!")
                .setChannelId("com.bestoffers")
        } else {
            builder = NotificationCompat.Builder(context, "com.bestoffers")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                .setContentTitle("O preço caiu!")
                .setContentText("${product.name} caiu de preço!")
        }

        notificationManager.notify(1234, builder.build())
    }
}