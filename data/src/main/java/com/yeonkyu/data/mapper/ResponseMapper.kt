package com.yeonkyu.data.mapper

import com.sendbird.calls.Participant
import com.sendbird.calls.RemoteParticipant
import com.yeonkyu.data.model.RoomResponse
import com.yeonkyu.domain.model.Member
import com.yeonkyu.domain.util.roomName

fun List<com.sendbird.calls.Room>.toRoomList() = this.map {
    it.toRoom()
}

fun com.sendbird.calls.Room.toRoom() = com.yeonkyu.domain.model.Room(
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

fun RoomResponse.toRoom() = com.yeonkyu.domain.model.Room(
    id = this.room.roomId,
    title = "",
    participants = emptyList()
)
