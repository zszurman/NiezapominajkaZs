package com.zszurman.niezapominajkazs

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addAdr
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addD
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addId
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addM
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addNot
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addR
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addTyt
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.bar
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.bat
import com.zszurman.niezapominajkazs.MainActivity.Companion.dupa
import kotlinx.android.synthetic.main.row.view.*

class CardViewAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<Nota> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(p0.context)
        val cardViewNote = layoutInflater.inflate(R.layout.row, p0, false)
        val dbHelper = DbHelper(context)

        return MyViewHolder(context, cardViewNote, dbHelper)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                holder.hold(list[position])
            }
        }
    }

    fun okList(osobyList: ArrayList<Nota>) {
        list = osobyList
    }
}

class MyViewHolder(context: Context, holder: View, dbHelper: DbHelper) :
    RecyclerView.ViewHolder(holder) {

    private val num = holder.nrTv!!
    private val tyt = holder.tytulTv!!
    private val not = holder.notaTv!!
    private val adr = holder.adresTv!!
    private val data = holder.dataTv!!
    private val rok = holder.rTv!!
    private val mies = holder.mTv!!
    private val dzien = holder.dTv!!
    private val place = holder.placeBtn!!
    private val edit = holder.editBtn!!
    private val delete = holder.deleteBtn!!

    fun hold(nota: Nota) {
        num.text = nota.nr.toString()
        tyt.text = nota.tytul
        not.text = nota.not
        adr.text = nota.adres
        rok.text = nota.r.toString()
        mies.text = nota.m.toString()
        dzien.text = nota.d.toString()
        data.text = nota.terminString()
        data.setTextColor(nota.kolor())
        addR = nota.r
        addM = nota.m
        addD = nota.d
        if (adr.text.isNullOrEmpty()) {
            place.visibility = View.INVISIBLE
            adr.height = 0
        }
    }

    init {

        place.setOnClickListener {

            val geoUri =
                "geo:50,20?q=" + adr.text.toString()
            val geo = Uri.parse(geoUri)
            val mapIntent = Intent(Intent.ACTION_VIEW, geo)
            holder.context.startActivity(mapIntent)
        }


        edit.setOnClickListener {

            addId = num.text.toString().toInt()
            addTyt = tyt.text.toString()
            addNot = not.text.toString()
            addAdr = adr.text.toString()
            addR = rok.text.toString().toInt()
            addM = mies.text.toString().toInt()
            addD = dzien.text.toString().toInt()
            bar = "Aktualizacja danych"
            bat = "Aktualizuj"
            dupa = 1

            val intent = Intent(context, AddNoteActivity::class.java)
            startActivity(context, intent, bundleOf())
        }

        delete.setOnClickListener {

            val id = num.text.toString().toInt()

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Uwaga")
            builder.setMessage("Chcesz usunąć wydarzenie")
            builder.setPositiveButton("Tak") { _, _ ->

                val selectionArgs = arrayOf(id.toString())
                dbHelper.delete(TableInfo.COL_ID + "=?", selectionArgs)

                dbHelper.initListAlfabet("%").sortWith(Comparator { o1, o2 ->
                    o1.obliczMillis().compareTo(o2.obliczMillis())
                })

                Toast.makeText(context, "Wydarzenie usunięto", Toast.LENGTH_SHORT)
                    .show()

                MainActivity.adapterNota?.notifyDataSetChanged()

                dupa = 1
                val intent = Intent(context, MainActivity::class.java)
                startActivity(context, intent, bundleOf())

            }
            builder.setNegativeButton("Nie") { _, _ ->
                Toast.makeText(
                    context,
                    "Nie usunięto wydarzenia",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

    }
}