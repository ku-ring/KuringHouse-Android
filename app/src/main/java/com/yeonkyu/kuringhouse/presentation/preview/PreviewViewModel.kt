package com.yeonkyu.kuringhouse.presentation.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.domain.model.Room
import com.yeonkyu.domain.usecase.room.CreateRoomUseCase
import com.yeonkyu.domain.usecase.room.GetRoomListUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val getRoomListUseCase: GetRoomListUseCase,
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val isEnd = MutableLiveData(false)

    private val _roomList = MutableLiveData<List<Room>>()
    val roomList: LiveData<List<Room>>
        get() = _roomList

    private val _dialogEvent = SingleLiveEvent<Int>()
    val dialogEvent: LiveData<Int>
        get() = _dialogEvent

    private val _dismissBottomSheetEvent = SingleLiveEvent<Unit>()
    val dismissBottomSheetEvent: LiveData<Unit>
        get() = _dismissBottomSheetEvent

    private val _createdRoomEvent = SingleLiveEvent<Room>()
    val createdRoomEvent: LiveData<Room>
        get() = _createdRoomEvent

    fun fetchRoomList() {
        isLoading.postValue(true)
        getRoomListUseCase.execute(
            onSuccess = {
                _roomList.postValue(it)
                isLoading.postValue(false)
            }, onError = { code, message ->
                Timber.e("getRoomList error [$code] : $message")
                isLoading.postValue(false)
            }, isEnd = {
                isEnd.value = true
            }
        )
    }

    fun refreshRoomList() {
        getRoomListUseCase.refresh()
        isEnd.value = false
    }

    fun createRoom(title: String) {
        createRoomUseCase.execute(
            title = title,
            onSuccess = {
                _dismissBottomSheetEvent.call()
                _createdRoomEvent.postValue(it)
            }, onError = { code, message ->
                Timber.e("createRoomList error [$code] : $message")
                _dialogEvent.postValue(R.string.create_room_fail)
                _dismissBottomSheetEvent.call()
            }
        )
    }
}