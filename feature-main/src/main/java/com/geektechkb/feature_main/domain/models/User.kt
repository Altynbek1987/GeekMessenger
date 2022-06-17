package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class User(
    val name: String? = null,
    val surname: String? = null,
    override val phoneNumber: String? = null,
    val profileImage: String? = null,
) : BaseDiffModel


