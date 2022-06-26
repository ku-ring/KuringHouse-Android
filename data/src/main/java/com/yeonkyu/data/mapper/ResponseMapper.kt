package com.yeonkyu.kuringhouse.data.mapper

import com.sendbird.calls.Participant
import com.sendbird.calls.RemoteParticipant
import com.sendbird.calls.Room
import com.yeonkyu.kuringhouse.data.model.RoomResponse
import com.yeonkyu.kuringhouse.domain.model.Member
import com.yeonkyu.kuringhouse.domain.util.roomName

fun List<Room>.toRoomList() = this.map {
    it.toRoom()
}

fun Room.toRoom() = com.yeonkyu.kuringhouse.domain.model.Room(
    id = this.roomId,
    title = this.customItems[roomName] ?: "",
    participants = this.participants.map { it.toMember() }
)

fun Participant.toMember() = Member(
    id = this.participantId,
    nickname = this.user.nickname ?: this.user.userId,
    profile = this.user.profileUrl ?: "",
    canSpeak = when (this) {
        is RemoteParticipant -> {
            this.isAudioEnabled
        }
        else -> {
            null
        }
    }
)

fun RoomResponse.toRoom() = com.yeonkyu.kuringhouse.domain.model.Room(
    id = this.room.roomId,
    title = "",
    participants = emptyList()
)
