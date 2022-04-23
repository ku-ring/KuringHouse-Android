package com.yeonkyu.kuringhouse.data.source.remote

import com.sendbird.calls.AuthenticateParams
import com.sendbird.calls.SendBirdCall
import com.yeonkyu.kuringhouse.domain.model.User

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