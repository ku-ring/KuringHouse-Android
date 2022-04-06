package com.yeonkyu.kuringhouse.di

import com.yeonkyu.kuringhouse.data.repository.LoginRepositoryImpl
import com.yeonkyu.kuringhouse.data.source.local.PreferenceManager
import com.yeonkyu.kuringhouse.data.source.remote.AuthClient
import com.yeonkyu.kuringhouse.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        pref: PreferenceManager,
        authClient: AuthClient
    ): LoginRepository {
        return LoginRepositoryImpl(pref, authClient)
    }
}