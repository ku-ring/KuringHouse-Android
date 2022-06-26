package com.yeonkyu.kuringhouse.domain.usecase.room

import com.yeonkyu.kuringhouse.domain.repository.RoomRepository
import com.yeonkyu.kuringhouse.domain.util.Result
import javax.inject.Inject

class LeaveRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend fun execute(roomId: String): Result<Unit> {
        return repository.exitRoom(roomId)
    }
}