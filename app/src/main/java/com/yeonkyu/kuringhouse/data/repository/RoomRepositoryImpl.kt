package com.yeonkyu.kuringhouse.data.repository

import com.yeonkyu.kuringhouse.data.mapper.toRoomList
import com.yeonkyu.kuringhouse.data.source.remote.RoomClient
import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.repository.RoomRepository

class RoomRepositoryImpl(
    private val roomClient: RoomClient
) : RoomRepository {
    override fun getRoomList(
        onSuccess: (List<Room>) -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit
    ) {
        roomClient.retrieveRoomList(
            onSuccess = {
                onSuccess(it.toRoomList())
            }, onError = { errorCode, errorMessage ->
                onError(errorCode, errorMessage)
            }
        )
    }
}