package com.example.rewardxlibrary.api

import com.example.rewardxlibrary.models.*
import retrofit2.http.*

interface RewardXApi {

    @POST("users")
    suspend fun createUser(@Body request: CreateUserRequest): ApiResponse<User>

    @GET("users")
    suspend fun getAllUsers(): List<User>

    @GET("users/{user_id}")
    suspend fun getUser(@Path("user_id") userId: String): User

    @GET("users/by-email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @GET("users/{user_id}/achievements")
    suspend fun getUserAchievements(@Path("user_id") userId: String): List<Achievement>

    @DELETE("users/{user_id}")
    suspend fun deleteUser(@Path("user_id") userId: String): ApiResponse<Unit>

    @POST("events")
    suspend fun trackEvent(@Body request: TrackEventRequest): EventTrackResponse

    @GET("events/{user_id}")
    suspend fun getUserEvents(@Path("user_id") userId: String): List<Event>

    @POST("achievements")
    suspend fun createAchievement(@Body request: CreateAchievementRequest): ApiResponse<Achievement>

    @GET("achievements")
    suspend fun getAllAchievements(): List<Achievement>

    @GET("achievements/{achievement_id}")
    suspend fun getAchievement(@Path("achievement_id") achievementId: String): Achievement

    @DELETE("achievements/{achievement_id}")
    suspend fun deleteAchievement(@Path("achievement_id") achievementId: String): ApiResponse<Unit>

    @POST("rules")
    suspend fun createRule(@Body request: CreateRuleRequest): ApiResponse<Rule>

    @GET("rules")
    suspend fun getAllRules(): List<Rule>

    @DELETE("rules/{rule_id}")
    suspend fun deleteRule(@Path("rule_id") ruleId: String): ApiResponse<Unit>
}