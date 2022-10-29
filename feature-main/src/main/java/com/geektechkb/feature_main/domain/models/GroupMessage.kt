package com.geektechkb.feature_main.domain.models

data class GroupMessage(
	val messageId: String? = null,
	val message: String? = null,
	val senderPhoneNumber: String? = null,
	val receiversPhoneNumbers: List<String>? = null,
	val timeMessageWasSent: String? = null,

	)