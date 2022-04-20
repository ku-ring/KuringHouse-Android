package com.yeonkyu.kuringhouse.data.source.remote

import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomListQueryResultHandler
import timber.log.Timber
import javax.inject.Inject

class RoomClient @Inject constructor(
    private val call: SendBirdCall,
    private val apiService: ApiService
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

    fun createRoom(
        params: RoomParams,
        onSuccess: (room: Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        call.createRoom(params) { room, e ->
            if (room == null || e != null) {
                onError(e?.code.toString(), e?.message ?: "")
            } else {
                onSuccess(room)
            }
        }
    }

    fun getRoomInfo(
        roomId: String,
        onSuccess: (room: Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        call.fetchRoomById(roomId) { room, e ->
            if (room == null || e != null) {
                onError(e?.code.toString(), e?.message ?: "")
            } else {
                onSuccess(room)
            }
        }
    }

    fun enterRoom(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        call.getCachedRoomById(roomId)?.run {
            for (participant in participants) {
                if (participant.user.userId == call.currentUser?.userId) {
                    onSuccess()
                    return@run
                }
            }

            val enterParams = EnterParams()
                .setAudioEnabled(false)
                .setVideoEnabled(false)

            enter(enterParams) { e ->
                if (e != null) {
                    onError(e.code.toString(), e.message ?: "")
                } else {
                    onSuccess()
                }
            }
        }
    }

    fun muteMic(roomId: String) {
        call.getCachedRoomById(roomId)?.run {
            // todo : re-implement - remove '!!'
            this.localParticipant!!.muteMicrophone()
            Timber.e("mute")
        }
    }

    fun unMuteMic(roomId: String) {
        call.getCachedRoomById(roomId)?.run {
            this.localParticipant!!.unmuteMicrophone()
            Timber.e("unMute")
        }
    }

    suspend fun exitRoom(roomId: String) {
        call.getCachedRoomById(roomId)?.run {
            this.exit()
            if (this.participants.isEmpty()) {
                try {
                    apiService.deleteRoom(roomId)
                } catch (e: Exception) {
                    Timber.e("delete room error : $e")
                }
            }
            Timber.e("room exited")
        }
    }
}