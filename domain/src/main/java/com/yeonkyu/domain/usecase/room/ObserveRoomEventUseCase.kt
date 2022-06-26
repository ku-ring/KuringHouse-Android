package com.yeonkyu.kuringhouse.domain.usecase.room

import com.yeonkyu.kuringhouse.domain.repository.RoomListenerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRoomEventUseCase @Inject constructor(
    private val repository: RoomListenerRepository
) {
    fun execute(): Flow<Unit> {
        return repository.getRoomUpdateEvent()
    }
}