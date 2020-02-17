package com.zszurman.niezapominajkazs
import java.util.*
object Harmonogram {
   val listH = addHarmonogram()
   private fun addHarmonogram(): ArrayList<Nota> {
        val listS = ArrayList<Nota>()
        var t = "Śmieci"
        var n = "Kubeł"
        val a = ""
        val r = 2020
        val i = 0
        listS.add(Nota(i, t, n, a, r, 2, 9))
        listS.add(Nota(i, t, n, a, r, 3, 14))
        listS.add(Nota(i, t, n, a, r, 4, 11))
        listS.add(Nota(i, t, n, a, r, 4, 25))
        listS.add(Nota(i, t, n, a, r, 5, 8))
        listS.add(Nota(i, t, n, a, r, 5, 22))
        listS.add(Nota(i, t, n, a, r, 6, 6))
        listS.add(Nota(i, t, n, a, r, 6, 20))
        listS.add(Nota(i, t, n, a, r, 7, 3))
        listS.add(Nota(i, t, n, a, r, 7, 17))
        listS.add(Nota(i, t, n, a, r, 7, 31))
        listS.add(Nota(i, t, n, a, r, 8, 14))
        listS.add(Nota(i, t, n, a, r, 8, 28))
        listS.add(Nota(i, t, n, a, r, 9, 12))
        listS.add(Nota(i, t, n, a, r, 10, 9))
        listS.add(Nota(i, t, n, a, r, 11, 7))
        n = "Popiół"
        listS.add(Nota(i, t, n, a, r, 1, 24))
        listS.add(Nota(i, t, n, a, r, 2, 23))
        listS.add(Nota(i, t, n, a, r, 3, 27))
        listS.add(Nota(i, t, n, a, r, 9, 26))
        listS.add(Nota(i, t, n, a, r, 10, 23))
        listS.add(Nota(i, t, n, a, r, 11, 21))
        n = "Gabaryty"
        listS.add(Nota(i, t, n, a, r, 4, 15))
        n = "Worki"
        listS.add(Nota(i, t, n, a, r, 2, 11))
        listS.add(Nota(i, t, n, a, r, 3, 8))
        listS.add(Nota(i, t, n, a, r, 4, 13))
        listS.add(Nota(i, t, n, a, r, 5, 10))
        listS.add(Nota(i, t, n, a, r, 6, 8))
        listS.add(Nota(i, t, n, a, r, 7, 12))
        listS.add(Nota(i, t, n, a, r, 8, 9))
        listS.add(Nota(i, t, n, a, r, 9, 14))
        listS.add(Nota(i, t, n, a, r, 10, 18))
        listS.add(Nota(i, t, n, a, r, 11, 9))
        t = "Emerytura"
        n = ":)"
        listS.add(Nota(i, t, n, a, r, 1, 25))
        listS.add(Nota(i, t, n, a, r, 2, 25))
        listS.add(Nota(i, t, n, a, r, 3, 24))
        listS.add(Nota(i, t, n, a, r, 4, 25))
        listS.add(Nota(i, t, n, a, r, 5, 25))
        listS.add(Nota(i, t, n, a, r, 6, 24))
        listS.add(Nota(i, t, n, a, r, 7, 25))
        listS.add(Nota(i, t, n, a, r, 8, 23))
        listS.add(Nota(i, t, n, a, r, 9, 25))
        listS.add(Nota(i, t, n, a, r, 10, 25))
        listS.add(Nota(i, t, n, a, r, 11, 25))
        t = "Prąd"
        n = "Podaj stan liczników"
        listS.add(Nota(i, t, n, a, r, 2, 5))
        listS.add(Nota(i, t, n, a, r, 3, 5))
        listS.add(Nota(i, t, n, a, r, 4, 5))
        listS.add(Nota(i, t, n, a, r, 5, 5))
        listS.add(Nota(i, t, n, a, r, 6, 5))
        listS.add(Nota(i, t, n, a, r, 7, 5))
        listS.add(Nota(i, t, n, a, r, 8, 5))
        listS.add(Nota(i, t, n, a, r, 9, 5))
        listS.add(Nota(i, t, n, a, r, 10, 5))
        listS.add(Nota(i, t, n, a, r, 11, 5))
        return listS
    }
}

