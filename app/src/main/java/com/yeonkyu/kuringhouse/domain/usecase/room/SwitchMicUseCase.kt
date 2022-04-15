package com.yeonkyu.kuringhouse.domain.usecase.room

import com.yeonkyu.kuringhouse.domain.repository.RoomRepository
import javax.inject.Inject

class SwitchMicUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun muteMic(roomId: String) {
        repository.muteMic(roomId)
    }

    fun unMuteMic(roomId: String) {
        repository.unMuteMic(roomId)
    }
}