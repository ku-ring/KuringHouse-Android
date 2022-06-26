package com.yeonkyu.kuringhouse.domain.repository

import com.yeonkyu.kuringhouse.domain.model.User

interface LoginRepository {
    fun getUser(): User
    fun updateUser(user: User)
    fun authorize(
        user: User,
        onSuccess: () -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit
    )
}