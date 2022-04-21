package com.yeonkyu.kuringhouse.domain.usecase.room

import com.yeonkyu.kuringhouse.domain.repository.RoomRepository
import javax.inject.Inject

class SwitchMicUseCase @Inject constructor(
    private val repository: RoomRepository
) {

    fun muteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        repository.muteMic(roomId, onSuccess, onError)
    }

    fun unMuteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        repository.unMuteMic(roomId, onSuccess, onError)
    }
}