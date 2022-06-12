package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class ReceivedMessage(
    override val id: String,
    val message: String,
    val timeMessageWasReceived: Long
) : BaseDiffModel<String>
