package com.example.rewardxlibrary.client

import com.example.rewardxlibrary.api.RewardXApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RewardXClient {

    private var baseUrl: String = ""
    private var isDebugMode: Boolean = false

    private val loggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = if (isDebugMode) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RewardXApi by lazy {
        retrofit.create(RewardXApi::class.java)
    }

    fun initialize(url: String, debug: Boolean = false) {
        baseUrl = if (url.endsWith("/")) url else "$url/"
        isDebugMode = debug
    }

    fun isInitialized(): Boolean = baseUrl.isNotEmpty()
}