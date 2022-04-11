package com.yeonkyu.kuringhouse.presentation.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.domain.model.Member
import com.yeonkyu.kuringhouse.domain.usecase.room.GetRoomInfoUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val getRoomInfoUseCase: GetRoomInfoUseCase
) : ViewModel() {

    lateinit var roomId: String
    val roomName = MutableLiveData<String>()
    val activeMemberList = MutableLiveData<List<Member>>()

    val dialogEvent = SingleLiveEvent<Int>()

    fun getRoomInfo(roomId: String) {
        getRoomInfoUseCase.execute(
            roomId = roomId,
            onSuccess = {
                activeMemberList.postValue(it.participants)
            }, onError = { code, message ->
                Timber.e("getRoomInfo error [$code] $message")
                dialogEvent.postValue(R.string.room_info_fail)
            })

    }
}