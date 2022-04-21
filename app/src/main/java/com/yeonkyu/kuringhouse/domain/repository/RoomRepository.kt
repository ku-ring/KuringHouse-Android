package com.yeonkyu.kuringhouse.domain.repository

import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.util.Result

interface RoomRepository {
    fun getRoomList(
        onSuccess: (List<Room>) -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit,
        isEnd: () -> Unit
    )

    fun refreshRoomList()

    fun createRoom(
        title: String,
        onSuccess: (room: Room) -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit
    )

    fun getRoomInfo(
        roomId: String,
        onSuccess: (Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    )

    fun enterRoom(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (code: String, message: String) -> Unit
    )

    fun muteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun unMuteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    suspend fun exitRoom(roomId: String): Result<Unit>
}