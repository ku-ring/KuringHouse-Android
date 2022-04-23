package com.yeonkyu.kuringhouse.data.source.remote

import com.sendbird.calls.*
import com.sendbird.calls.handler.RoomListQueryResultHandler
import com.yeonkyu.kuringhouse.domain.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RoomClient @Inject constructor(
    private val apiService: ApiService
) {

    private val params = RoomListQuery.Params()
        .setType(RoomType.LARGE_ROOM_FOR_AUDIO_ONLY)
        .setLimit(10)
        .setState(RoomState.OPEN)

    private var query = SendBirdCall.createRoomListQuery(params)

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
        query = SendBirdCall.createRoomListQuery(params)
    }

    fun createRoom(
        params: RoomParams,
        onSuccess: (room: Room) -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        SendBirdCall.createRoom(params) { room, e ->
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
        SendBirdCall.fetchRoomById(roomId) { room, e ->
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
        SendBirdCall.getCachedRoomById(roomId)?.run {
            for (participant in participants) {
                if (participant.user.userId == SendBirdCall.currentUser?.userId) {
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

    fun muteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        SendBirdCall.getCachedRoomById(roomId)?.run {
            this.localParticipant?.let {
                it.muteMicrophone()
                onSuccess()
            } ?: onError("getLocalParticipant fail")
        } ?: onError("getCachedRoomById fail")
    }

    fun unMuteMic(
        roomId: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) {
        SendBirdCall.getCachedRoomById(roomId)?.run {
            this.localParticipant?.let {
                it.unmuteMicrophone()
                onSuccess()
            } ?: onError("getLocalParticipant fail")
        } ?: onError("getCachedRoomById fail")
    }

    suspend fun exitRoom(roomId: String): Result<Unit> = withContext(Dispatchers.IO) {
        SendBirdCall.getCachedRoomById(roomId)?.run {
            this.exit()
            if (this.participants.isEmpty()) {
                deleteRoom(this.roomId)
            } else {
                Timber.e("room exited")
                Result.Success(Unit)
            }
        } ?: Result.Error(Exception("getCachedRoomById() fail"))
    }

    private suspend fun deleteRoom(roomId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteRoom(roomId)
            if (response.isSuccessful) {
                Timber.e("room exited")
                return@withContext Result.Success(Unit)
            } else {
                return@withContext Result.Error(Exception("delete room fail : ${response.message()}"))
            }
        } catch (e: Exception) {
            Timber.e("delete room error : $e")
            return@withContext Result.Error(e)
        }
    }
}