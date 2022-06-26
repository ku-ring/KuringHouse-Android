package com.yeonkyu.domain.usecase.auth

import com.yeonkyu.domain.model.User
import com.yeonkyu.domain.repository.LoginRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    fun execute(
        onSuccess: (User) -> Unit
    ) {
        onSuccess(repository.getUser())
    }
}