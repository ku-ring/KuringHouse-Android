package com.yeonkyu.kuringhouse.domain.usecase.auth

import com.yeonkyu.kuringhouse.domain.model.User
import com.yeonkyu.kuringhouse.domain.repository.LoginRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    fun execute(id: String, accessToken: String) {
        val user = User(id, accessToken)
        repository.updateUser(user)
    }
}