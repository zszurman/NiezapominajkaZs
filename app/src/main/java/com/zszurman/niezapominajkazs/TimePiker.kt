package com.zszurman.niezapominajkazs


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addD
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addM
import com.zszurman.niezapominajkazs.AddNoteActivity.Companion.addR


class TimePiker : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return DatePickerDialog(
            requireActivity(),
            activity as DatePickerDialog.OnDateSetListener,
            addR,
            addM,
            addD
        )
    }
}