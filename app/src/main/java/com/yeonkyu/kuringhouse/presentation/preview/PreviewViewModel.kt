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

    private val _roomList = ArrayList<Room>()
    val roomList = MutableLiveData<List<Room>>()

    fun getRoomList() {
        getRoomUseCase.execute(
            onSuccess = {
                _roomList.addAll(it)
                roomList.postValue(_roomList)
            }, onError = { code, message ->
                Timber.e("getRoomList error [$code] : $message")
            }
        )
    }
}