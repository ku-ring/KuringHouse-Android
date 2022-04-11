package com.yeonkyu.kuringhouse.presentation.room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class RoomViewModel @Inject constructor(

) : ViewModel() {

    val roomName = MutableLiveData<String>()
    
}