package com.zszurman.niezapominajkazs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.core.app.NotificationCompat
import com.zszurman.niezapominajkazs.MainActivity.Companion.alarmWhen
import com.zszurman.niezapominajkazs.MainActivity.Companion.list

class AlarmReceiver : BroadcastReceiver() {

    private lateinit var notificationChannel: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private val channelId = "com.zszurman.niezapominajkazs"
    private val description = "Powiadomienia o wydarzeniach"

    override fun onReceive(context: Context, intent: Intent?) {

        val dbHelper = DbHelper(context)

        fun initRecord(cursor: Cursor) {
            val nr = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_ID))
            val tyt = cursor.getString(cursor.getColumnIndex(TableInfo.COL_TYT))
            val not = cursor.getString(cursor.getColumnIndex(TableInfo.COL_NOT))
            val adr = cursor.getString(cursor.getColumnIndex(TableInfo.COL_ADR))
            val r = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_R))
            val m = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_M))
            val d = cursor.getInt(cursor.getColumnIndex(TableInfo.COL_D))
            val x = Nota(nr, tyt, not, adr, r, m, d)
            list.add(x)
        }

        val selectionArgs = arrayOf("%")
        val cursor = dbHelper.qery(
            MainActivity.projections,
            TableInfo.COL_TYT + " like ?",
            selectionArgs,
            TableInfo.COL_TYT
        )
        list.clear()

        if (cursor.moveToFirst()) {
            do {
                initRecord(cursor)
            } while (cursor.moveToNext())
        }



        var idData = 0
        var xx: String
        var yy: String

        while (idData < list.size) {
            if (list[idData].obliczDoAlarmu() == alarmWhen) {
                xx = list[idData].tytul
                yy = list[idData].not
                alarm(context, idData, xx, yy)

            }
            idData++
        }
    }

    private fun alarm(context: Context?, idData: Int, xx: String, yy: String) {

        val intentZs = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, intentZs, 0)

        notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationChannel =
            NotificationCompat.Builder(context, channelId)
                .setContentTitle(xx)
                .setContentText(yy)
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
