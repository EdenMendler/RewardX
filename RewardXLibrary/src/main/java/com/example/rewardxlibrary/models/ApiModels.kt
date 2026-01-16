package com.example.rewardxlibrary.models

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("message")
    val message: String? = null,

    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("error")
    val error: String? = null
)

data class EventTrackResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("_id")
    val eventId: String,

    @SerializedName("new_achievements")
    val newAchievements: List<Achievement> = emptyList()
)

data class CreateUserRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String
)

data class TrackEventRequest(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("event_type")
    val eventType: String,

    @SerializedName("event_data")
    val eventData: Map<String, Any>
)

data class CreateAchievementRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("points")
    val points: Int,

    @SerializedName("icon")
    val icon: String
)

data class CreateRuleRequest(
    @SerializedName("achievement_id")
    val achievementId: String,

    @SerializedName("event_type")
    val eventType: String,

    @SerializedName("condition_type")
    val conditionType: String,

    @SerializedName("threshold")
    val threshold: Int,

    @SerializedName("field")
    val field: String? = null
)