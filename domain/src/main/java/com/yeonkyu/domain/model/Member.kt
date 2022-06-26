package com.yeonkyu.kuringhouse.domain.model

data class Member(
    val id: String,
    val nickname: String,
    val profile: String,
    val canSpeak: Boolean?
)