package com.zszurman.niezapominajkazs

import android.app.*
import android.content.Context
import android.content.Intent
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
        var list: ArrayList<Nota> = Baza.utworzLista()
        var total: Int = 1
        var godzina: Int = 8
        var minuta: Int = 0
        var adapterNota: CardViewAdapter? = null
        var dupa: Int = 0
        var alarmTime: Long = System.currentTimeMillis() - 10000
        var alarmDay: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (dupa == 1) finish()
        dupa = 0
        setPref()
        initRecyclerView(startBazaNajblizsze())
        setActionBar(total)
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
        if (mActionBar != null)
            when (total) {
                0 -> mActionBar.subtitle = "Nie masz wydarzeń"
                1 -> mActionBar.subtitle = "Masz 1 wydarzenie"
                else -> mActionBar.subtitle = "Ilość wydarzeń: $total"
            }
    }

    private fun setPref() {
        val preferencja = Preferencje(this)
        godzina = preferencja.getGodz()
        minuta = preferencja.getMin()
        alarmDay = preferencja.getPrefAlarmDay()
    }

    private fun initAlarm() {
        startBazaNajblizsze()
        val pref = Preferencje(this)
        alarmDay = pref.getPrefAlarmDay()
        val x = (alarmDay * (24 * 60 * 60 * 1000)).toLong()
        if (!list.isNullOrEmpty()) {
            var i = 0
            do {
                alarmTime = list[i].obliczMillis()
                i++
            } while (alarmTime < (System.currentTimeMillis() + x) && i < list.size)
        }
        if (alarmTime > (System.currentTimeMillis() + x)) {
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentZs = Intent(applicationContext, AlarmReceiver::class.java)
            alarmIntent = PendingIntent.getBroadcast(applicationContext, 0, intentZs, 0)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                alarmTime - x,
                alarmIntent
            )
        }
    }

    private fun startBazaNajblizsze(): ArrayList<Nota> {
        val dbHelper = DbHelper(this)
        dbHelper.initListAlfabet("%").sortWith(Comparator { o1, o2 ->
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
        val dbHelper = DbHelper(this@MainActivity)
        val sv: SearchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                initRecyclerView(dbHelper.initListAlfabet("%$query%"))
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                initRecyclerView(dbHelper.initListAlfabet("%$newText%"))
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
                    dupa = 1
                    val intent = Intent(applicationContext, AddNoteActivity::class.java)
                    startActivity(intent)
                }
                R.id.action_sort -> showSortDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSortDialog() {
        val dbHelper = DbHelper(this)
        val pref = Preferencje(this)
        alarmDay = pref.getPrefAlarmDay()
        val czas = if (minuta < 10) ("$godzina:0$minuta alarm")
        else "$godzina:$minuta alarm"
        val jutro = if (alarmDay == 0) "alarm w dniu wydarzenia"
        else "alarm dzień wcześniej"

        val sortOptions =
            arrayOf("na osi czasu", "alfabetycznie", "resetuj dane", czas, jutro)
        val mBilder = AlertDialog.Builder(this)
        mBilder.setTitle("Wybierz")
        mBilder.setIcon(R.drawable.ic_action_options)
        mBilder.setSingleChoiceItems(sortOptions, -1) { dialogInterface, i ->
            when (i) {
                0 -> {
                    Toast.makeText(this, "Na osi czasu", Toast.LENGTH_SHORT).show()
                    initRecyclerView(startBazaNajblizsze())
                }
                1 -> {
                    Toast.makeText(this, "Alfabetycznie", Toast.LENGTH_SHORT).show()
                    initRecyclerView(dbHelper.initListAlfabet("%"))
                }
                2 -> {
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
                3 -> {
                    val timePiker = AlarmPiker()
                    timePiker.show(supportFragmentManager, "alarm piker")
                }
                4 -> {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Ustaw czas alarmu")
                    builder.setMessage("Dzień wcześniej?")
                    builder.setPositiveButton("Tak") { _, _ ->
                        alarmDay = 1
                        pref.setPrefAlarmDay(alarmDay)
                        Toast.makeText(
                            applicationContext,
                            "Alarm dzień wcześniej",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    builder.setNegativeButton("Nie") { _, _ ->
                        alarmDay = 0
                        pref.setPrefAlarmDay(alarmDay)
                        Toast.makeText(
                            applicationContext,
                            "Alarm w dniu wydarzenia",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
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

