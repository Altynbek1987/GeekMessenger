package com.geektechkb.feature_auth.data.apiServise

import com.geektechkb.common.constants.Constants.CONTENT_TYPE
import com.geektechkb.common.constants.Constants.SERVER_KEY
import com.geektechkb.feature_auth.presentation.model.PushNotification
import com.squareup.okhttp.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface NotificationApi {
    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}