package com.yeonkyu.data.di

import com.yeonkyu.data.repository.LoginRepositoryImpl
import com.yeonkyu.data.repository.RoomListenerRepositoryImpl
import com.yeonkyu.data.repository.RoomRepositoryImpl
import com.yeonkyu.data.source.local.PreferenceManager
import com.yeonkyu.data.source.remote.AuthClient
import com.yeonkyu.data.source.remote.RoomClient
import com.yeonkyu.data.source.remote.RoomEventListener
import com.yeonkyu.domain.repository.LoginRepository
import com.yeonkyu.domain.repository.RoomListenerRepository
import com.yeonkyu.domain.repository.RoomRepository
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

//    @Binds
//    abstract fun provideLoginRepository(
//        impl: LoginRepositoryImpl
//    ): LoginRepository

    @Provides
    @Singleton
    fun provideRoomRepository(
        roomClient: RoomClient
    ): RoomRepository {
        return RoomRepositoryImpl(roomClient)
    }

//    @Binds
//    abstract fun provideRoomRepository(impl: RoomRepositoryImpl): RoomRepository

    @Provides
    @Singleton
    fun provideRoomListenerRepository(
        roomEventListener: RoomEventListener
    ): RoomListenerRepository {
        return RoomListenerRepositoryImpl(roomEventListener)
    }

//    @Binds
//    abstract fun provideRoomListenerRepository(impl: RoomListenerRepositoryImpl): RoomListenerRepository
}