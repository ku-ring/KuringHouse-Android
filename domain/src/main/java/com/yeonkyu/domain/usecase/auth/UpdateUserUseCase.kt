package com.yeonkyu.domain.usecase.auth

import com.yeonkyu.domain.model.User
import com.yeonkyu.domain.repository.LoginRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    fun execute(id: String, accessToken: String) {
        val user = User(id, accessToken)
        repository.updateUser(user)
    }
}