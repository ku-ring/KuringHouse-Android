package com.yeonkyu.kuringhouse.presentation.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.domain.model.Member
import com.yeonkyu.kuringhouse.domain.usecase.room.EnterRoomUseCase
import com.yeonkyu.kuringhouse.domain.usecase.room.GetRoomInfoUseCase
import com.yeonkyu.kuringhouse.domain.usecase.room.LeaveRoomUseCase
import com.yeonkyu.kuringhouse.domain.usecase.room.SwitchMicUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val enterRoomInfoUseCase: EnterRoomUseCase,
    private val switchMicUseCase: SwitchMicUseCase,
    private val leaveRoomUseCase: LeaveRoomUseCase
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
    val dialogEvent: SingleLiveEvent<Int>
        get() = _dialogEvent

    private val _quitEvent = SingleLiveEvent<Unit>()
    val quitEvent: SingleLiveEvent<Unit>
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
            }, onError = { code, message ->
                Timber.e("enterRoom error [$code] : $message")
                _dialogEvent.postValue(R.string.enter_room_fail)
            })
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
            switchMicUseCase.muteMic(roomId)
            _isMicOn.postValue(false)
        } else {
            switchMicUseCase.unMuteMic(roomId)
            _isMicOn.postValue(true)
        }
    }

    fun leaveRoom(roomId: String) {
        viewModelScope.launch {
            try {
                leaveRoomUseCase.execute(roomId)
                _quitEvent.call()
            } catch (e: Exception) {
                Timber.e("leaveRoom error : $e")
                _dialogEvent.postValue(R.string.leave_room_fail)
            }
        }
    }
}