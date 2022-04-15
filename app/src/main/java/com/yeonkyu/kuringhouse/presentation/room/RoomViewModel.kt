package com.yeonkyu.kuringhouse.presentation.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.domain.model.Member
import com.yeonkyu.kuringhouse.domain.usecase.room.EnterRoomUseCase
import com.yeonkyu.kuringhouse.domain.usecase.room.GetRoomInfoUseCase
import com.yeonkyu.kuringhouse.domain.usecase.room.SwitchMicUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val getRoomInfoUseCase: GetRoomInfoUseCase,
    private val enterRoomInfoUseCase: EnterRoomUseCase,
    private val switchMicUseCase: SwitchMicUseCase
) : ViewModel() {

    lateinit var roomId: String
    val roomName = MutableLiveData<String>()

    private val _activeMemberList = MutableLiveData<List<Member>>()
    val activeMemberList: LiveData<List<Member>>
        get() = _activeMemberList

    private val _isMicOn = MutableLiveData(false)
    val isMicOn: LiveData<Boolean>
        get() = _isMicOn

    val dialogEvent = SingleLiveEvent<Int>()

    fun getRoom(roomId: String) {
        getRoomInfoUseCase.execute(
            roomId = roomId,
            onSuccess = {
                enterRoom(it.id)
            }, onError = { code, message ->
                Timber.e("getRoom error [$code] : $message")
                dialogEvent.postValue(R.string.room_info_fail)
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
                dialogEvent.postValue(R.string.enter_room_fail)
            })
    }

    private fun getRoomInfo(roomId: String) {
        getRoomInfoUseCase.execute(
            roomId = roomId,
            onSuccess = {
                _activeMemberList.postValue(it.participants)
            }, onError = { code, message ->
                Timber.e("getRoomInfo error [$code] : $message")
                dialogEvent.postValue(R.string.room_info_fail)
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
}