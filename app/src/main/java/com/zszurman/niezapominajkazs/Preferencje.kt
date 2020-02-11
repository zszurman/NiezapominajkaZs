package com.zszurman.niezapominajkazs


import android.content.Context


class Preferencje(context: Context) {

    private val PREF_NAME = "Preferencja"
    private val PREF_GODZ = "Godzina"
    private val PREF_MIN = "Minuta"

    private val preferencja = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    fun getGodz(): Int {
        return preferencja.getInt(PREF_GODZ, 0)
    }
    fun setGodz(godzina: Int) {
        val editor = preferencja.edit()
        editor.putInt(PREF_GODZ, godzina)
        editor.apply()
    }

    fun getMin(): Int {
        return preferencja.getInt(PREF_MIN, 0)
    }
    fun setMin(minuta: Int) {
        val editor = preferencja.edit()
        editor.putInt(PREF_MIN, minuta)
        editor.apply()
    }
}