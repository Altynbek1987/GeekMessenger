package com.geektechkb.feature_main.data.remote.services.apiservices

import com.geektechkb.common.constants.Constants.CONTENT_TYPE
import com.geektechkb.common.constants.Constants.SERVER_KEY
import com.geektechkb.feature_main.domain.models.PushNotification
import com.squareup.okhttp.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationsApi {
	@Headers("Authorization: key=$SERVER_KEY", "content_type:$CONTENT_TYPE")
	@POST("fcm/send")
	suspend fun postNotifications(
		@Body notificationsService: PushNotification
	): Response<ResponseBody>
}