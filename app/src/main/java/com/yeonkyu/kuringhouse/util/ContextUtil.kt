package com.yeonkyu.kuringhouse.util

import android.app.Dialog
import android.content.Context
import com.yeonkyu.kuringhouse.presentation.dialogs.KuringHouseDialog

fun Context.makeDialog(mainText: String, subText: String?): Dialog {
    val dialog = KuringHouseDialog(this)
        .setText(title = mainText, description = subText)
    dialog.show()
    return dialog
}

fun Context.makeDialog(mainText: String) = makeDialog(mainText, null)