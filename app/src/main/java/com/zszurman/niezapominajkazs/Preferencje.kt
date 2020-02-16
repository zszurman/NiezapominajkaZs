package com.zszurman.niezapominajkazs

import android.content.Context


class Preferencje(context: Context) {

    private val prefName = "Preferencja"
    private val prefGodz = "Godzina"
    private val prefMin = "Minuta"
    private val prefAlarmDay = "Jutro"

    private val preferencja = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun getGodz(): Int {
        return preferencja.getInt(prefGodz, 0)
    }
    fun setGodz(godzina: Int) {
        val editor = preferencja.edit()
        editor.putInt(prefGodz, godzina)
        editor.apply()
    }

    fun getMin(): Int {
        return preferencja.getInt(prefMin, 0)
    }
    fun setMin(minuta: Int) {
        val editor = preferencja.edit()
        editor.putInt(prefMin, minuta)
        editor.apply()
    }
    fun getPrefAlarmDay(): Int {
        return preferencja.getInt(prefAlarmDay, 0)
    }
    fun setPrefAlarmDay(dzien: Int) {
        val editor = preferencja.edit()
        editor.putInt(prefAlarmDay, dzien)
        editor.apply()
    }

}