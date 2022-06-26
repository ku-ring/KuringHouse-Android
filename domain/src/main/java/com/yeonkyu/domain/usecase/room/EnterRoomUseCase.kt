package com.yeonkyu.domain.usecase.room

import com.yeonkyu.domain.model.Room
import com.yeonkyu.domain.repository.RoomRepository
import javax.inject.Inject

class EnterRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    fun execute(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        repository.enterRoom(
            roomId = roomId,
            onSuccess = {
                onSuccess()
            }, onError = { code, message ->
                onError(code, message)
            })
    }
}