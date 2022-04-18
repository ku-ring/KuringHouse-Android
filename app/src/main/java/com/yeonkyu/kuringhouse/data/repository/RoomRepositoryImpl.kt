package com.yeonkyu.kuringhouse.data.repository

import com.sendbird.calls.RoomParams
import com.sendbird.calls.RoomType
import com.yeonkyu.kuringhouse.data.mapper.toRoom
import com.yeonkyu.kuringhouse.data.mapper.toRoomList
import com.yeonkyu.kuringhouse.data.source.remote.RoomClient
import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.repository.RoomRepository
import com.yeonkyu.kuringhouse.domain.util.roomName

class RoomRepositoryImpl(
    private val roomClient: RoomClient
) : RoomRepository {

    private val roomList = ArrayList<com.sendbird.calls.Room>()

    override fun getRoomList(
        onSuccess: (List<Room>) -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit,
        isEnd: () -> Unit
    ) {
        roomClient.retrieveRoomList(
            onSuccess = {
                for (room in it) {
                    roomList.add(room)
                }
                onSuccess(roomList.toRoomList())
            }, onError = { errorCode, errorMessage ->
                onError(errorCode, errorMessage)
            }, isEnd = {
                isEnd()
            }
        )
    }

    override fun refreshRoomList() {
        roomList.clear()
        roomClient.refreshRoomList()
    }

    override fun createRoom(
        title: String,
        onSuccess: (Room) -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit
    ) {
        val customItems: Map<String, String> = mapOf(Pair(roomName, title))
        val params = RoomParams(RoomType.LARGE_ROOM_FOR_AUDIO_ONLY)
            .setCustomItems(customItems)

        roomClient.createRoom(
            params = params,
            onSuccess = {
                onSuccess(it.toRoom())
            }, onError = { code, message ->
                onError(code, message)
            }
        )
    }

    override fun getRoomInfo(
        roomId: String,
        onSuccess: (Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        roomClient.getRoomInfo(
            roomId = roomId,
            onSuccess = {
                onSuccess(it.toRoom())
            }, onError = { code, message ->
                onError(code, message)
            })
    }

    override fun enterRoom(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        roomClient.enterRoom(
            roomId = roomId,
            onSuccess = {
                onSuccess()
            }, onError = { code, message ->
                onError(code, message)
            })
    }

    override fun muteMic(roomId: String) {
        roomClient.muteMic(roomId)
    }

    override fun unMuteMic(roomId: String) {
        roomClient.unMuteMic(roomId)
    }

    override suspend fun exitRoom(roomId: String) {
        roomClient.exitRoom(roomId)
    }
}