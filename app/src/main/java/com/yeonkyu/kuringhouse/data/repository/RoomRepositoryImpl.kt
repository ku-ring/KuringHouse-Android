package com.yeonkyu.kuringhouse.data.repository

import com.sendbird.calls.RoomParams
import com.sendbird.calls.RoomType
import com.yeonkyu.kuringhouse.data.mapper.toRoom
import com.yeonkyu.kuringhouse.data.mapper.toRoomList
import com.yeonkyu.kuringhouse.data.source.remote.RoomClient
import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.repository.RoomRepository

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
        val customItems: Map<String, String> = mapOf(Pair("name", title))
        val params = RoomParams(RoomType.LARGE_ROOM_FOR_AUDIO_ONLY)
            .setCustomItems(customItems)

        roomClient.createRoom(
            params = params
            , onSuccess = {
                onSuccess(it.toRoom())
            }, onError = { code, message ->
                onError(code, message)
            }
        )
    }
}