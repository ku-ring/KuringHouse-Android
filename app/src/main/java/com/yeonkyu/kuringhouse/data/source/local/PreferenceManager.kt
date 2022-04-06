package com.yeonkyu.kuringhouse.data.source.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

class PreferenceManager(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("kuring_house_prefs", Context.MODE_PRIVATE)

    var id: String
        get() = prefs.getString(ID, null) ?: ""
        set(value) = prefs.edit().putString(ID, value).apply()

    var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, null) ?: ""
        set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    companion object {
        const val ID = "ID"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
    }
}