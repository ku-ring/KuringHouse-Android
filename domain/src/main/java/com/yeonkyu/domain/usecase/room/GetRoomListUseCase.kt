package com.yeonkyu.domain.usecase.room

import com.yeonkyu.domain.model.Room
import com.yeonkyu.domain.repository.RoomRepository
import javax.inject.Inject

class GetRoomListUseCase @Inject constructor(
    private val repository: RoomRepository
) {
    fun execute(
        onSuccess: (List<Room>) -> Unit,
        onError: (code: String, message: String) -> Unit,
        isEnd: () -> Unit
    ) {
        repository.getRoomList(onSuccess, onError, isEnd)
    }

    fun refresh() {
        repository.refreshRoomList()
    }
}