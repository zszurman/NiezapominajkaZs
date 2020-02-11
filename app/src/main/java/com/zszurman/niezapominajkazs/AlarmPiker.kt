package com.zszurman.niezapominajkazs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import com.zszurman.niezapominajkazs.MainActivity.Companion.godzina
import com.zszurman.niezapominajkazs.MainActivity.Companion.minuta

class AlarmPiker: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
            requireActivity(),
            activity as TimePickerDialog.OnTimeSetListener,
            godzina,
            minuta,
            DateFormat.is24HourFormat(activity)
        )
    }
}