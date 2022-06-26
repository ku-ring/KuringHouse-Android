package com.yeonkyu.data.repository

import com.sendbird.calls.RoomParams
import com.sendbird.calls.RoomType
import com.yeonkyu.data.mapper.toRoom
import com.yeonkyu.data.mapper.toRoomList
import com.yeonkyu.data.source.remote.RoomClient
import com.yeonkyu.domain.model.Room
import com.yeonkyu.domain.repository.RoomRepository
import com.yeonkyu.domain.util.Result
import com.yeonkyu.domain.util.roomName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
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
                    if (room.participants.isNotEmpty()) {
                        roomList.add(room)
                    }
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

    override fun muteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        roomClient.muteMic(roomId, onSuccess, onError)
    }

    override fun unMuteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        roomClient.unMuteMic(roomId, onSuccess, onError)
    }

    override suspend fun exitRoom(
        roomId: String,
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            roomClient.exitRoom(roomId)
        }
    }
}