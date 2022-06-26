package com.yeonkyu.data.repository

import com.yeonkyu.data.source.local.PreferenceManager
import com.yeonkyu.data.source.remote.AuthClient
import com.yeonkyu.domain.model.User
import com.yeonkyu.domain.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
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