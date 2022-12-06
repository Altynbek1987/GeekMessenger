package com.geektechkb.feature_main.data.remote.services.instence

import com.geektechkb.common.constants.Constants
import com.geektechkb.feature_main.data.remote.services.apiservices.NotificationsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
	companion object {
		private val retrofit by lazy {
			Retrofit.Builder()
				.baseUrl(Constants.BASE_URL)
				.addConverterFactory(GsonConverterFactory.create())
				.build()
		}
		val notificationApi by lazy {
			retrofit.create(NotificationsApi::class.java)
		}
	}
}