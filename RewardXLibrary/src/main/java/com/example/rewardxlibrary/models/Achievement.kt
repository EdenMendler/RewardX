package com.example.rewardxlibrary.models

import com.google.gson.annotations.SerializedName

data class Achievement(
    @SerializedName("_id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("points")
    val points: Int,

    @SerializedName("icon")
    val icon: String,

    @SerializedName("created_at")
    val createdAt: String? = null
)