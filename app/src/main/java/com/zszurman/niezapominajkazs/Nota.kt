package com.zszurman.niezapominajkazs


import android.graphics.Color
import java.util.*

object Baza {
    fun utworzLista(): ArrayList<Nota> {
        val lista = ArrayList<Nota>()
        lista.add(
            Nota(
                0,
                "Wycieczka",
                "wyjazd do Jaworza",
                "44-352 Czyżowice",
                2020,
                5,
                25
            )
        )
        lista.add(
            Nota(
                1,
                "Wujazd",
                "dupa",
                "44-352 Czyżowice",
                2021,
                5,
                25
            )
        )

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
        0 -> " lat, "
        1 -> " rok, "
        2 -> " lata, "
        3 -> " lata, "
        4 -> " lata, "
        else -> " lat, "
    }

    private fun obDay(): String? {

        return when (obliczDDoTerminu()) {
            1 -> " dzień)"
            else -> " dni)"
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

        kolorek = if ((30 > xx) && (xx > 14) && (xx == 0))
            Color.rgb(0, 0, 255)
        else if ((14 >= xx) && (xx > 7) && (xx == 0))
            Color.rgb(150, 0, 255)
        else if ((7 >= xx) && (xx > 1) && (xx == 0))
            Color.rgb(200, 0, 50)
        else if ((1 >= xx) && (xx >= 0) && (xx == 0))
            Color.rgb(255, 0, 0)
        else if (xx < 0)
            Color.rgb(0, 255, 100)
        else
            Color.rgb(0, 0, 0)
        return kolorek

    }

    fun terminString(): String {
        val x: String = if (m > 8) d.toString() + "." + (m + 1).toString() + "." + r.toString() + " ("
        else d.toString() + ".0" + (m + 1).toString() + "." + r.toString() + " ("



        return when {
            obliczLataDoTerminu()==0 && obliczDDoTerminu()==0 -> x + obliczDDoTerminu().toString() + obDay()
            System.currentTimeMillis() > obliczMillis() -> x + "Było minęło)"
            obliczLataDoTerminu() > 0 -> x + obliczLataDoTerminu().toString() + obAlat() + obliczDDoTerminu() + obDay()
            obliczLataDoTerminu()==0 -> x + obliczDDoTerminu().toString() + obDay()
            else -> ""
        }


    }

}


