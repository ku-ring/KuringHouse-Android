package com.yeonkyu.kuringhouse

import android.app.Application
import com.sendbird.calls.SendBirdCall
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class KuringHouseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        SendBirdCall.init(applicationContext, com.yeonkyu.data.BuildConfig.SENDBIRD_APP_ID)
    }
}