package com.yeonkyu.domain.usecase.room

import com.yeonkyu.domain.model.Room
import com.yeonkyu.domain.repository.RoomRepository
import javax.inject.Inject

class CreateRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    fun execute(
        title: String,
        onSuccess: (room: Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        repository.createRoom(
            title = title,
            onSuccess = {
                onSuccess(it)
            }, onError = { code, message ->
                onError(code, message)
            })
    }
}