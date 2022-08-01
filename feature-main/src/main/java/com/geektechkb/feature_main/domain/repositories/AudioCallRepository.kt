package com.geektechkb.feature_main.domain.repositories

interface AudioCallRepository {
    fun authenticateToSendbird(userId: String, accessToken: String)
}