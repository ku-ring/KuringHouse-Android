package com.yeonkyu.kuringhouse.presentation.preview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.domain.model.Room
import com.yeonkyu.kuringhouse.domain.usecase.room.GetRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val getRoomUseCase: GetRoomUseCase
) : ViewModel() {

    val isLoading = MutableLiveData<Boolean>(false)
    val isEnd = MutableLiveData<Boolean>(false)

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
    }
}