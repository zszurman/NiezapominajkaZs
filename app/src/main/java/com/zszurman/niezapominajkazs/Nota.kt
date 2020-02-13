package com.zszurman.niezapominajkazs


import android.graphics.Color
import java.util.*

object Baza {
    fun utworzLista(): ArrayList<Nota> {
        val lista = ArrayList<Nota>()
        lista.add(Nota(0, "Niezapominajka", "Masz dzisiaj wydarzenie", "x", 2020, 5, 25))

        return lista
    }

}

class Nota(
    var nr: Int = 0,
    var tytul: String = "",
    var not: String = "",
    var adres: String = "",
    var r: Int = 2020,
    var m: Int = 0,
    var d: Int = 1

) {

    fun obliczDoAlarmu(): Int {


        return when {
            obliczDDoTerminu() + 365 * obliczLataDoTerminu() == 0 -> 0
            System.currentTimeMillis() > obliczMillis() -> obliczDDoTerminu() + 36500 * obliczLataDoTerminu()
            else -> obliczDDoTerminu() + 365 * obliczLataDoTerminu()
        }


    }

    fun nTime(hour: Int, min: Int): Long {

        val date = Calendar.getInstance()
        date.set(Calendar.MONTH, m)
        date.set(Calendar.DAY_OF_MONTH, d)
        date.set(Calendar.HOUR_OF_DAY, hour)
        date.set(Calendar.MINUTE, min)
        date.set(Calendar.SECOND, 0)
        date.set(Calendar.YEAR, r)

        return date.timeInMillis
    }

    fun obliczMillis(): Long {
        val waznosc = Calendar.getInstance()
        waznosc.set(Calendar.YEAR, r)
        waznosc.set(Calendar.MONTH, m)
        waznosc.set(Calendar.DAY_OF_MONTH, d)

        return waznosc.timeInMillis
    }

    fun kolor(): Int {

        val kolorek: Int
        val xx = obliczDDoTerminu()

        kolorek = if ((8 > xx) && (xx > 2))
            Color.rgb(0, 0, 255)
        else if (xx == 2)
            Color.rgb(150, 0, 255)
        else if (xx == 1)
            Color.rgb(200, 0, 50)
        else if (xx == 0)
            Color.rgb(255, 0, 0)
        else if (System.currentTimeMillis() > obliczMillis())
            Color.rgb(93, 98, 111)
        else
            Color.rgb(0, 0, 0)
        return kolorek

    }

    fun terminString(): String {
        val x: String =
            if (m > 8) d.toString() + "." + (m + 1).toString() + "." + r.toString() + "  "
            else d.toString() + ".0" + (m + 1).toString() + "." + r.toString() + "  "



        return when {
            obliczLataDoTerminu() == 0 && obliczDDoTerminu() == 0 -> x + "dzisiaj"
            obliczLataDoTerminu() == 0 && obliczDDoTerminu() == 1 -> x + "jutro"
            obliczLataDoTerminu() == 0 && obliczDDoTerminu() == 2 -> x + "pojutrze"
            System.currentTimeMillis() > obliczMillis() -> x + "było minęło"
            obliczLataDoTerminu() > 0 -> x + "za " + obliczLataDoTerminu().toString() + obAlat() + obliczDDoTerminu() + obDay()
            obliczLataDoTerminu() == 0 -> x + "za" + obliczDDoTerminu().toString() + obDay()
            else -> ""
        }


    }

    private fun obliczDDoTerminu(): Int {

        val teraz = Calendar.getInstance()
        val dziDY = teraz[Calendar.DAY_OF_YEAR]
        val dziY = teraz[Calendar.YEAR]
        val ostatni = Calendar.getInstance()
        ostatni[Calendar.MONTH] = 11
        ostatni[Calendar.DAY_OF_MONTH] = 31
        val ostDY = ostatni[Calendar.DAY_OF_YEAR]
        val umowa = Calendar.getInstance()
        umowa[Calendar.MONTH] = m
        umowa[Calendar.DAY_OF_MONTH] = d
        umowa[Calendar.YEAR] = r
        val umowaDY = umowa[Calendar.DAY_OF_YEAR]
        return if (r < dziY || r == dziY && dziDY > umowaDY) -100 else if (umowaDY < dziDY && r > dziY) umowaDY - dziDY + ostDY else umowaDY - dziDY
    }

    private fun obAlat(): String? = when (obliczLataDoTerminu()) {
        0 -> " lat i "
        1 -> " rok i "
        2 -> " lata i "
        3 -> " lata i "
        4 -> " lata i "
        else -> " lat i "
    }

    private fun obDay(): String? {

        return when (obliczDDoTerminu()) {
            1 -> " dzień"
            else -> " dni"
        }
    }

    private fun obliczLataDoTerminu(): Int {

        val teraz = Calendar.getInstance()
        val dziDY = teraz[Calendar.DAY_OF_YEAR]
        val dziY = teraz[Calendar.YEAR]
        val umowa = Calendar.getInstance()
        umowa[Calendar.MONTH] = m
        umowa[Calendar.DAY_OF_MONTH] = d
        umowa[Calendar.YEAR] = r
        val umowaDY = umowa[Calendar.DAY_OF_YEAR]
        return if (umowaDY >= dziDY) r - dziY else r - dziY - 1
    }

}


