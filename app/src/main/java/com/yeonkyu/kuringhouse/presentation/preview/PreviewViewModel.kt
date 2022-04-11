package com.yeonkyu.kuringhouse.presentation.preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.R
import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.usecase.room.CreateRoomUseCase
import com.yeonkyu.kuringhouse.domain.usecase.room.GetRoomUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val getRoomUseCase: GetRoomUseCase,
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val isEnd = MutableLiveData(false)

    val dialogEvent = SingleLiveEvent<Int>()
    val dismissBottomSheetEvent = SingleLiveEvent<Unit>()

    val roomList = MutableLiveData<List<Room>>()

    init {
        getRoomList()
    }

    fun getRoomList() {
        isLoading.postValue(true)
        getRoomUseCase.execute(
            onSuccess = {
                roomList.postValue(it)
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
        getRoomUseCase.refresh()
        isEnd.value = false
    }

    fun createRoom(title: String) {
        createRoomUseCase.execute(
            title = title,
            onSuccess = {
                refreshRoomList()
                getRoomList()
                dismissBottomSheetEvent.call()
            }, onError = { code, message ->
                Timber.e("createRoomList error [$code] $message")
                dialogEvent.postValue(R.string.create_room_fail)
                dismissBottomSheetEvent.call()
            }
        )
    }
}