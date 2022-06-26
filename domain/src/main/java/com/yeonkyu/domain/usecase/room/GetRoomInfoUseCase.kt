package com.yeonkyu.domain.usecase.room

import com.yeonkyu.domain.model.Room
import com.yeonkyu.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomInfoUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    fun execute(
        roomId: String,
        onSuccess: (Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        repository.getRoomInfo(roomId, onSuccess, onError)
    }
}