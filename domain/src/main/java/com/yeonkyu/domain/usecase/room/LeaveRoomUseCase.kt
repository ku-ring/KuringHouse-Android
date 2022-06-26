package com.yeonkyu.domain.usecase.room

import com.yeonkyu.domain.repository.RoomRepository
import com.yeonkyu.domain.util.Result
import javax.inject.Inject

class LeaveRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    suspend fun execute(roomId: String): Result<Unit> {
        return repository.exitRoom(roomId)
    }
}