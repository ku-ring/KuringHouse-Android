package com.yeonkyu.kuringhouse.data.source.remote

import com.sendbird.calls.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RoomEventListener : RoomListener {
    private val _updateEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val updateEvent: SharedFlow<Unit>
        get() = _updateEvent

    fun addListener(tag: String, roomId: String) {
        SendBirdCall.getCachedRoomById(roomId)?.addListener(tag, this)
    }

    fun removeListener(tag: String, roomId: String) {
        SendBirdCall.getCachedRoomById(roomId)?.removeListener(tag)
    }

    override fun onCustomItemsDeleted(deletedKeys: List<String>) {
        Timber.e("onCustomItemsDeleted called. deletedKeys : $deletedKeys")
    }

    override fun onCustomItemsUpdated(updatedKeys: List<String>) {
        Timber.e("onCustomItemsUpdated called. updatedKeys : $updatedKeys")
    }

    override fun onRemoteAudioSettingsChanged(participant: RemoteParticipant) {
        Timber.e("onRemoteAudioSettingChanged called. participant : ${participant.user.userId}, canSpeak : ${participant.isAudioEnabled}")
        CoroutineScope(Dispatchers.IO).launch {
            _updateEvent.emit(Unit)
        }
    }

    override fun onRemoteParticipantEntered(participant: RemoteParticipant) {
        Timber.e("onRemoteParticipantEntered")
        CoroutineScope(Dispatchers.IO).launch {
            _updateEvent.emit(Unit)
        }
    }

    override fun onRemoteParticipantExited(participant: RemoteParticipant) {
        Timber.e("onRemoteParticipantExited")
        CoroutineScope(Dispatchers.IO).launch {
            _updateEvent.emit(Unit)
        }
    }

    override fun onError(e: SendBirdException, participant: Participant?) {
        Timber.e("onError called to ${participant?.user?.userId} [${e.code}] : ${e.message}")
    }

    override fun onRemoteParticipantStreamStarted(participant: RemoteParticipant) = Unit

    override fun onRemoteVideoSettingsChanged(participant: RemoteParticipant) = Unit

    override fun onAudioDeviceChanged(currentAudioDevice: AudioDevice?, availableAudioDevices: Set<AudioDevice>) = Unit

}