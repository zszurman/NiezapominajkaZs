package com.zszurman.niezapominajkazs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.zszurman.niezapominajkazs.MainActivity.Companion.findFirst
import com.zszurman.niezapominajkazs.MainActivity.Companion.list

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var notificationChannel: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private val channelId = "com.zszurman.niezapominajkazs"
    private val description = "Powiadomienia o wydarzeniach"

    override fun onReceive(context: Context?, intent: Intent?) {

        var idData = 0

        while (idData < list.size) {
            if (list[idData].obliczDoAlarmu() == findFirst())
                alarm(context, idData)
            idData++
        }

    }

    private fun alarm(context: Context?, idData: Int) {

        val intentZs = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, intentZs, 0)

        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationChannel =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(list[idData].tytul)
                .setContentText(list[idData].not)
                .setSmallIcon(R.drawable.ic_action_date)
                .setContentIntent(contentIntent)

        val channel =
            NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(idData, notificationChannel.build())
        }

    }

}
