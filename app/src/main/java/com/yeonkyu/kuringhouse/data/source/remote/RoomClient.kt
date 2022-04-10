package com.yeonkyu.kuringhouse.data.source.remote

import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomListQueryResultHandler
import javax.inject.Inject

class RoomClient @Inject constructor(
    private val call: SendBirdCall
) {
    fun retrieveRoomList(
        onSuccess: (List<Room>) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        val params = RoomListQuery.Params()
            .setType(RoomType.LARGE_ROOM_FOR_AUDIO_ONLY)
            .setLimit(20)
            .setState(RoomState.OPEN)

        val query = call.createRoomListQuery(params)
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
}