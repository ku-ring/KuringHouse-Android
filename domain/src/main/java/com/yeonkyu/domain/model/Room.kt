package com.yeonkyu.domain.model

data class Room(
    val id: String,
    val title: String,
    val participants: List<Member>
)