package com.yeonkyu.kuringhouse.presentation.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.domain.model.Member
import com.yeonkyu.kuringhouse.domain.usecase.room.*
import com.yeonkyu.kuringhouse.domain.util.succeeded
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val enterRoomInfoUseCase: EnterRoomUseCase,
    private val switchMicUseCase: SwitchMicUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase,
    private val addRoomListenerUseCase: AddRoomListenerUseCase,
    private val removeRoomListenerUseCase: RemoveRoomListenerUseCase,
    private val observeRoomEventUseCase: ObserveRoomEventUseCase
) : ViewModel() {

    lateinit var roomId: String
    val roomName = MutableLiveData<String>()

    private val _activeMemberList = MutableLiveData<List<Member>>()
    val activeMemberList: LiveData<List<Member>>
        get() = _activeMemberList

    private val _isMicOn = MutableLiveData(false)
    val isMicOn: LiveData<Boolean>
        get() = _isMicOn

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val _quitEvent = SingleLiveEvent<Int?>()
    val quitEvent: LiveData<Int?>
        get() = _quitEvent

    fun getRoom(roomId: String) {
        getRoomInfoUseCase.execute(
            roomId = roomId,
            onSuccess = {
                enterRoom(it.id)
            }, onError = { code, message ->
                Timber.e("getRoom error [$code] : $message")
                _dialogEvent.postValue(R.string.room_info_fail)
            })
    }

    private fun enterRoom(roomId: String) {
        enterRoomInfoUseCase.execute(
            roomId = roomId,
            onSuccess = {
                Timber.e("enterRoom success")
                getRoomInfo(roomId)
                observeRoomEvent()
            }, onError = { code, message ->
                Timber.e("enterRoom error [$code] : $message")
                _dialogEvent.postValue(R.string.enter_room_fail)
            })
    }

    private fun observeRoomEvent() {
        addRoomListenerUseCase.execute(TAG, roomId)

        viewModelScope.launch(Dispatchers.IO) {
            observeRoomEventUseCase.execute()
                .collect { getRoomInfo(roomId) }
        }
    }

    private fun getRoomInfo(roomId: String) {
        getRoomInfoUseCase.execute(
            roomId = roomId,
            onSuccess = {
                _activeMemberList.postValue(it.participants)
            }, onError = { code, message ->
                Timber.e("getRoomInfo error [$code] : $message")
                _dialogEvent.postValue(R.string.room_info_fail)
            })
    }

    fun switchMic() {
        if (isMicOn.value == true) {
            switchMicUseCase.muteMic(
                roomId = roomId,
                onSuccess = {
                    _isMicOn.postValue(false)
                }, onError = {
                    _quitEvent.postValue(R.string.connection_fail)
                })
        } else {
            switchMicUseCase.unMuteMic(
                roomId = roomId,
                onSuccess = {
                    _isMicOn.postValue(true)
                }, onError = {
                    _quitEvent.postValue(R.string.connection_fail)
                })
        }
    }

    fun leaveRoom(roomId: String) {
        viewModelScope.launch {
            try {
                val response = leaveRoomUseCase.execute(roomId)
                if (response.succeeded) {
                    _quitEvent.call()
                } else {
                    _quitEvent.postValue(R.string.leave_room_fail)
                }
            } catch (e: Exception) {
                Timber.e("leaveRoom error : $e")
                _quitEvent.postValue(R.string.leave_room_fail)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        removeRoomListenerUseCase.execute(TAG, roomId)
    }

    companion object {
        val TAG: String = RoomViewModel::class.java.simpleName
    }
}