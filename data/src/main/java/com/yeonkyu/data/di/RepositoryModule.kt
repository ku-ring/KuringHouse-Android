package com.yeonkyu.data.di

import com.yeonkyu.kuringhouse.data.repository.LoginRepositoryImpl
import com.yeonkyu.kuringhouse.data.repository.RoomListenerRepositoryImpl
import com.yeonkyu.kuringhouse.data.repository.RoomRepositoryImpl
import com.yeonkyu.kuringhouse.data.source.local.PreferenceManager
import com.yeonkyu.kuringhouse.data.source.remote.AuthClient
import com.yeonkyu.kuringhouse.data.source.remote.RoomClient
import com.yeonkyu.kuringhouse.data.source.remote.RoomEventListener
import com.yeonkyu.kuringhouse.domain.repository.LoginRepository
import com.yeonkyu.kuringhouse.domain.repository.RoomListenerRepository
import com.yeonkyu.kuringhouse.domain.repository.RoomRepository
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

    @Provides
    @Singleton
    fun provideRoomRepository(
        roomClient: RoomClient
    ): RoomRepository {
        return RoomRepositoryImpl(roomClient)
    }

    @Provides
    @Singleton
    fun provideRoomListenerRepository(
        roomEventListener: RoomEventListener
    ): RoomListenerRepository {
        return RoomListenerRepositoryImpl(roomEventListener)
    }
}