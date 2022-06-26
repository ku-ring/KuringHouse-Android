package com.yeonkyu.kuringhouse.domain.repository

import kotlinx.coroutines.flow.Flow

interface RoomListenerRepository {
    fun addListener(tag: String, roomId: String)
    fun removeListener(tag: String, roomId: String)
    fun getRoomUpdateEvent(): Flow<Unit>
}