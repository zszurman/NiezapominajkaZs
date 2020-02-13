package com.zszurman.niezapominajkazs

import android.app.*
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addAdr
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addD
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addM
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addNot
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addR
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addTyt
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.bar
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.bat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    companion object {
        val projections = arrayOf(
            TableInfo.COL_ID, TableInfo.COL_TYT, TableInfo.COL_NOT, TableInfo.COL_ADR,
            TableInfo.COL_R, TableInfo.COL_M, TableInfo.COL_D
        )
        var list: ArrayList<Nota>  = Baza.utworzLista()
        var total: Int = list.size
        var godzina: Int = 8
        var minuta: Int = 0
        var adapterNota: CardViewAdapter? = null
        var dupa: Int = 0
        fun findFirst(): Int {

            var record = list[0].obliczDoAlarmu()
            fun min(a: Int, b: Int): Int = if (a < b) a else b
            var number = 0

            while (number < (list.size - 1)) {
                record = min(record, list[number + 1].obliczDoAlarmu())

                number++
            }

            return record

        }
        fun timeM():Long{

            var yy:Long = list[0].nTime(godzina, minuta)
            var number=0
            while (number < (list.size - 1)) {
                if (list[number].obliczDoAlarmu() == findFirst()){
                    yy = list[number].nTime(godzina,minuta)
                }
                number++
            }

            return yy

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (dupa == 1) finish()
        dupa = 0



        initRecyclerView(startBazaNajblizsze())
        setActionBar(total)
        setPref()
        initAlarm()

    }

    override fun onResume() {
        super.onResume()
        initRecyclerView(startBazaNajblizsze())
        setActionBar(total)
        setPref()

    }

    override fun onDestroy() {
        super.onDestroy()
        initAlarm()
    }

    private fun initRecyclerView(list: ArrayList<Nota>) {
        notes.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 1)
            adapterNota = CardViewAdapter(this@MainActivity)
            adapter = adapterNota
        }
        adapterNota?.okList(list)
        adapterNota?.notifyDataSetChanged()
    }

    private fun setActionBar(total: Int) {

        val mActionBar = supportActionBar

        if (mActionBar != null) {
            when (total) {
                0 -> {
                    mActionBar.subtitle = "Nie masz wydarzeń"

                }
                1 -> {
                    mActionBar.subtitle = "Masz 1 wydarzenie"
                }
                else -> {
                    mActionBar.subtitle = "Ilość wydarzeń: $total "
                }
            }
        }
    }

    private fun setPref() {

        val preferencja = Preferencje(this)
        godzina = preferencja.getGodz()
        minuta = preferencja.getMin()
    }

    private fun initAlarm() {

        if (! list.isNullOrEmpty()) {
            timeM()
            findFirst()
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentZs = Intent(applicationContext, AlarmReceiver::class.java)
            alarmIntent = PendingIntent.getBroadcast(applicationContext, 0, intentZs, 0)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                timeM(),
                alarmIntent
            )
        }
    }

    private fun startBaza(title: String): ArrayList<Nota> {

        val dbHelper = DbHelper(this)

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

        val selectionArgs = arrayOf(title)
        val cursor = dbHelper.qery(
            projections,
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
        total = list.size
        return list

    }

    private fun startBazaNajblizsze(): ArrayList<Nota> {
        startBaza("%").sortWith(Comparator { o1, o2 ->
            o1.obliczMillis().compareTo(o2.obliczMillis())
        })

        return list
    }

    private fun startBazaReset() {

        val dbHelper = DbHelper(this)
        dbHelper.deleteData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                initRecyclerView(startBaza("%$query%"))
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                initRecyclerView(startBaza("%$newText%"))
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    addTyt = ""
                    addNot = ""
                    addAdr = ""
                    val c = Calendar.getInstance()
                    addR = c[Calendar.YEAR]
                    addM = c[Calendar.MONTH]
                    addD = c[Calendar.DAY_OF_MONTH]
                    bar = "Dodaj wydarzenie"
                    bat = "Dodaj"

                    finish()
                    val intent = Intent(applicationContext, AddNoteActivity::class.java)
                    startActivity(intent)

                }
                R.id.action_sort -> {
                    showSortDialog()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {

        val czas = if (minuta < 10) ("$godzina:0$minuta alarm")
        else "$godzina:$minuta alarm"

        val sortOptions =
            arrayOf("na osi czasu", "alfabetycznie", "resetuj dane", czas)
        val mBilder = AlertDialog.Builder(this)
        mBilder.setTitle("Wybierz")
        mBilder.setIcon(R.drawable.ic_action_options)
        mBilder.setSingleChoiceItems(sortOptions, -1) { dialogInterface, i ->
            if (i == 0) {
                Toast.makeText(this, "Na osi czasu", Toast.LENGTH_SHORT).show()
                initRecyclerView(startBazaNajblizsze())
            }
            if (i == 1) {
                Toast.makeText(this, "Alfabetycznie", Toast.LENGTH_SHORT).show()
                initRecyclerView(startBaza("%"))
            }

            if (i == 2) {

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Uwaga")
                builder.setMessage("Chcesz zresetować dane")

                builder.setPositiveButton("Tak") { _, _ ->
                    startBazaReset()
                    onResume()
                    Toast.makeText(applicationContext, "Zresetowano dane", Toast.LENGTH_SHORT)
                        .show()
                }

                builder.setNegativeButton("Nie") { _, _ ->
                    Toast.makeText(
                        applicationContext,
                        "Nie zresetowano danych",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
            if (i == 3) {

                val timePiker = AlarmPiker()
                timePiker.show(supportFragmentManager, "alarm piker")

            }

            dialogInterface.dismiss()
        }
        val mDialog = mBilder.create()
        mDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        godzina = hourOfDay
        minuta = minute

        val preferencja = Preferencje(this)
        preferencja.setGodz(godzina)
        preferencja.setMin(minuta)

    }
}

