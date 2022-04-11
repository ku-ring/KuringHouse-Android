package com.yeonkyu.kuringhouse.data.mapper

import com.sendbird.calls.Room
import com.yeonkyu.kuringhouse.domain.util.roomName

fun List<Room>.toRoomList() = this.map {
    it.toRoom()
}

fun Room.toRoom() = com.yeonkyu.kuringhouse.domain.model.Room(
    id = this.roomId,
    title = this.customItems[roomName] ?: "",
    participants = this.participants.size.toString()
)