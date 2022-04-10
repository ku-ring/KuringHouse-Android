package com.yeonkyu.kuringhouse.domain.usecase.room

import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    fun execute(
        onSuccess: (List<Room>) -> Unit,
        onError: (code: String, message: String) -> Unit,
        isEnd: () -> Unit
    ) {
        repository.getRoomList(onSuccess, onError, isEnd)
    }
}