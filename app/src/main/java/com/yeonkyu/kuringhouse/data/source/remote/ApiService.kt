package com.yeonkyu.kuringhouse.data.source.remote

import com.yeonkyu.kuringhouse.data.model.RoomResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {

    @DELETE("v1/rooms/{room_id}")
    suspend fun deleteRoom(
        @Path("room_id") roomId: String
    ): Response<RoomResponse>
}