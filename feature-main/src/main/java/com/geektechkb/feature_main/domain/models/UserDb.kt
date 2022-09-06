package com.geektechkb.feature_main.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.geektechkb.core.base.BaseDiffModel
@Entity
 data class UserDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val secondName: String,
    override val phoneNumber: String?,
) : BaseDiffModel
