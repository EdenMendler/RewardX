package com.example.rewardxlibrary.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id")
    val id: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("total_points")
    val totalPoints: Int = 0,

    @SerializedName("achievements")
    val achievements: List<String> = emptyList()
)