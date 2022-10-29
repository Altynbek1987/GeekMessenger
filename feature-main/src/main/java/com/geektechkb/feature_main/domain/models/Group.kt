package com.geektechkb.feature_main.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val groupId: String? = null,
    val groupMembers: List<User>? = null,
    val groupName: String? = null,
    val groupPhoto: String? = null,
    val userCount: Int? = null,
    val userNumber: String? = null
)