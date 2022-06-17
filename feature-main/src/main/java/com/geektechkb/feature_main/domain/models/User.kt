package com.geektechkb.feature_main.domain.models

import com.geektechkb.core.base.BaseDiffModel

data class User(
    val name: String = " ",
    val surname: String = " ",
    override val phoneNumber: String = " ",
    val profileImage: String = " ",
) : BaseDiffModel<String> {

}
