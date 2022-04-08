package com.yeonkyu.kuringhouse.presentation.login

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

    val authSuccess = SingleLiveEvent<Unit>()
    val dialogEvent = SingleLiveEvent<Pair<String, String?>>()

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
            dialogEvent.postValue(Pair("이메일을 입력해주세요.", null))
            return
        }
        if (accessToken.isEmpty()) {
            dialogEvent.postValue(Pair("초대 코드를 입력해주세요.", null))
            return
        }

        authorizeUseCase.execute(
            user = User(email, accessToken),
            onSuccess = {
                Timber.e("SB auth success")
                saveUser(email, accessToken)
                authSuccess.call()
            },
            onError = { code, message ->
                Timber.e("SB auth fail : [$code] $message")
                dialogEvent.postValue(Pair("로그인 실패", message))
            }
        )
    }
}