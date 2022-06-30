package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class User(
    val name: String? = null,
    val lastName: String? = null,
    override val phoneNumber: String? = null,
    val profileImage: String? = null,
    val lastSeen: String? = null,
    val latestMessages: ArrayList<Message>? = null
) : BaseDiffModel


