package com.yeonkyu.kuringhouse.di

import com.sendbird.calls.SendBirdCall
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SendBirdModule {

    @Provides
    @Singleton
    fun provideSendBirdCallModule(): SendBirdCall {
        return SendBirdCall
    }
}