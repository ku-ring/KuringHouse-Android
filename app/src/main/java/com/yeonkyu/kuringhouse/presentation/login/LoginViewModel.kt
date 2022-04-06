package com.yeonkyu.kuringhouse.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.domain.model.User
import com.yeonkyu.kuringhouse.domain.usecase.auth.GetUserUseCase
import com.yeonkyu.kuringhouse.domain.usecase.auth.UpdateUserUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {

    val id = MutableLiveData<String>()
    val accessToken = MutableLiveData<String>()

    val dialogEvent = SingleLiveEvent<String>()

    init {
        getUser()
    }

    private fun getUser() {
        getUserUseCase.execute(
            onSuccess = { user ->
                id.postValue(user.id)
                accessToken.postValue(user.accessToken)
            }
        )
    }

    fun saveUser(email: String, accessToken: String) {
        updateUserUseCase.execute(email, accessToken)
    }

}