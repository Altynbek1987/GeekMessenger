package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class Message(
    override val id: String? = null,
    val receiverPhoneNumber: String? = null,
    val message: String? = null,
    val timeMessageWasSent: String? = null
) : BaseDiffModel<String>
