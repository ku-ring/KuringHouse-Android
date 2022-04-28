package com.yeonkyu.kuringhouse.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeonkyu.kuringhouse.domain.model.User
import com.yeonkyu.kuringhouse.domain.usecase.auth.AuthorizeUseCase
import com.yeonkyu.kuringhouse.domain.usecase.auth.GetUserUseCase
import com.yeonkyu.kuringhouse.domain.usecase.auth.UpdateUserUseCase
import com.yeonkyu.kuringhouse.presentation.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val authorizeUseCase: AuthorizeUseCase
) : ViewModel() {

    val email = MutableLiveData<String>()
    val accessToken = MutableLiveData<String>()

    private val _authSuccess = SingleLiveEvent<Unit>()
    val authSuccess: LiveData<Unit>
        get() = _authSuccess

    private val _dialogEvent = SingleLiveEvent<Pair<String, String?>>()
    val dialogEvent: LiveData<Pair<String, String?>>
        get() = _dialogEvent

    init {
        getUser()
    }

    private fun getUser() {
        getUserUseCase.execute(
            onSuccess = { user ->
                email.postValue(user.id)
                accessToken.postValue(user.accessToken)
            }
        )
    }

    private fun saveUser(email: String, accessToken: String) {
        updateUserUseCase.execute(email, accessToken)
    }

    fun authorize() {
        val email = email.value?.trim() ?: ""
        val accessToken = accessToken.value?.trim() ?: ""

        if (email.isEmpty()) {
            _dialogEvent.postValue(Pair("이메일을 입력해주세요.", null))
            return
        }
        if (accessToken.isEmpty()) {
            _dialogEvent.postValue(Pair("초대 코드를 입력해주세요.", null))
            return
        }

        authorizeUseCase.execute(
            user = User(email, accessToken),
            onSuccess = {
                Timber.e("SB auth success")
                saveUser(email, accessToken)
                _authSuccess.call()
            },
            onError = { code, message ->
                Timber.e("SB auth fail : [$code] $message")
                _dialogEvent.postValue(Pair("로그인 실패", message))
            }
        )
    }
}