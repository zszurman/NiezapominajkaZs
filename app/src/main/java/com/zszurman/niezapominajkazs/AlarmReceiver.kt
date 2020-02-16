package com.zszurman.niezapominajkazs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.zszurman.niezapominajkazs.MainActivity.Companion.alarmDay
import com.zszurman.niezapominajkazs.MainActivity.Companion.list

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var notificationChannel: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private val channelId = "com.zszurman.niezapominajkazs"
    private val description = "Powiadomienia o wydarzeniach"

    override fun onReceive(context: Context, intent: Intent?) {
        val dbHelper = DbHelper(context)
        dbHelper.initListAlfabet("%")
        val pref = Preferencje(context)
        alarmDay = pref.getPrefAlarmDay()
        var i = 0
        val d = when(alarmDay){
            0 -> "Dzisiaj: "
            1 -> "Jutro: "
           else ->""
        }
        while (i < list.size) {
            if (list[i].obliczDoAlarmu() == alarmDay) {
                alarm(context, i, d + list[i].tytul, list[i].not)
            }
            i++
        }
    }

    private fun alarm(context: Context?, i: Int, t: String, n: String) {
        val intentZs = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, intentZs, 0)
        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationChannel =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(t)
                .setContentText(n)
                .setSmallIcon(R.drawable.ic_action_date)
                .setContentIntent(contentIntent)
        val channel =
            NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(i, notificationChannel.build())
        }
    }
}
