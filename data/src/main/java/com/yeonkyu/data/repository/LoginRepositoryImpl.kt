package com.yeonkyu.kuringhouse.data.repository

import com.yeonkyu.kuringhouse.data.source.local.PreferenceManager
import com.yeonkyu.kuringhouse.data.source.remote.AuthClient
import com.yeonkyu.kuringhouse.domain.model.User
import com.yeonkyu.kuringhouse.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val pref: PreferenceManager,
    private val authClient: AuthClient
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

    override fun authorize(
        user: User,
        onSuccess: () -> Unit,
        onError: (errorCode: String, errorMessage: String) -> Unit
    ) {
        authClient.authenticate(user, onSuccess, onError)
    }
}