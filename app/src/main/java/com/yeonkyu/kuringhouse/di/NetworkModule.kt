package com.yeonkyu.kuringhouse.di

import com.sendbird.calls.SendBirdCall
import com.yeonkyu.kuringhouse.data.source.remote.AuthClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthClient(call: SendBirdCall): AuthClient {
        return AuthClient(call)
    }
}