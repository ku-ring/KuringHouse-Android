package com.yeonkyu.kuringhouse.domain.repository

import com.yeonkyu.kuringhouse.domain.model.User

interface LoginRepository {
    fun getUser(): User
    fun updateUser(user: User)
}