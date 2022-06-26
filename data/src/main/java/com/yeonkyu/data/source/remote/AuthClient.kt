package com.yeonkyu.data.source.remote

import com.sendbird.calls.AuthenticateParams
import com.sendbird.calls.SendBirdCall
import com.yeonkyu.domain.model.User

class AuthClient {
    fun authenticate(
        user: User,
        onSuccess: () -> Unit,
        onError: (code: String, message: String) -> Unit
    ) {
        val params = AuthenticateParams(userId = user.id)
            .setAccessToken(accessToken = user.accessToken)

        SendBirdCall.authenticate(params = params) { _, e ->
            if (e == null) {
                onSuccess()
            } else {
                onError(e.code.toString(), e.message ?: "")
            }
        }
    }
}