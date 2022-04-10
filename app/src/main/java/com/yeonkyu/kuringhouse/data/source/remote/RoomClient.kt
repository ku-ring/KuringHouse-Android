package com.yeonkyu.kuringhouse.data.source.remote

import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomListQueryResultHandler
import javax.inject.Inject

class RoomClient @Inject constructor(
    private val call: SendBirdCall
) {

    private val params = RoomListQuery.Params()
        .setType(RoomType.LARGE_ROOM_FOR_AUDIO_ONLY)
        .setLimit(10)
        .setState(RoomState.OPEN)

    private var query = call.createRoomListQuery(params)

    fun retrieveRoomList(
        onSuccess: (List<Room>) -> Unit,
        onError: (code: String, message: String) -> Unit,
        isEnd: () -> Unit
    ) {
        if (!query.hasNext()) {
            isEnd()
        }

        query.next(object : RoomListQueryResultHandler {
            override fun onResult(rooms: List<Room>, e: SendBirdException?) {
                if (e != null) {
                    onError(e.code.toString(), e.message ?: "")
                    return
                }
                onSuccess(rooms)
            }
        })
    }

    fun refreshRoomList() {
        query = call.createRoomListQuery(params)
    }
}