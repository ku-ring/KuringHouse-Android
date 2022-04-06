package com.yeonkyu.kuringhouse.domain.usecase.auth

import com.yeonkyu.kuringhouse.domain.model.User
import com.yeonkyu.kuringhouse.domain.repository.LoginRepository
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