package com.yeonkyu.data.repository

import com.yeonkyu.data.source.remote.RoomEventListener
import com.yeonkyu.domain.repository.RoomListenerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomListenerRepositoryImpl @Inject constructor(
    private val listener: RoomEventListener
) : RoomListenerRepository {

    override fun addListener(tag: String, roomId: String) {
        listener.addListener(tag, roomId)
    }

    override fun removeListener(tag: String, roomId: String) {
        listener.removeListener(tag, roomId)
    }

    override fun getRoomUpdateEvent(): Flow<Unit> {
        return listener.updateEvent
    }
}