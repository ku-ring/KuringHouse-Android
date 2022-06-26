package com.yeonkyu.data.model

import com.google.gson.annotations.SerializedName

data class RoomResponse(
    @SerializedName("room")
    val room: RoomData
)

data class RoomData(
    @SerializedName("room_id")
    val roomId: String,
    @SerializedName("state")
    val state: String
)
