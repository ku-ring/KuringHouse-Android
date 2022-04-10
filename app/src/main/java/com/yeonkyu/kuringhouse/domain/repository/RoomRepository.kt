package com.yeonkyu.kuringhouse.domain.repository

import com.yeonkyu.kuringhouse.domain.model.Room

interface RoomRepository {
    fun getRoomList(
        onSuccess: (List<Room>) -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit,
        isEnd: () -> Unit
    )

    fun refreshRoomList()
}