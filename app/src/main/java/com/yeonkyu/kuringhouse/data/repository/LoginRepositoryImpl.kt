package com.yeonkyu.kuringhouse.data.repository

import com.yeonkyu.kuringhouse.data.source.local.PreferenceManager
import com.yeonkyu.kuringhouse.domain.model.User
import com.yeonkyu.kuringhouse.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val pref: PreferenceManager
) : LoginRepository {

    override fun getUser(): User {
        val id = pref.id
        val accessToken = pref.accessToken
        return User(id, accessToken)
    }

    override fun updateUser(user: User) {
        pref.id = user.id
        pref.accessToken = user.accessToken
    }

}