package com.example.rewardxlibrary.models

import com.google.gson.annotations.SerializedName

data class Rule(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("achievement_id")
    val achievementId: String,

    @SerializedName("event_type")
    val eventType: String,

    @SerializedName("condition_type")
    val conditionType: String,

    @SerializedName("threshold")
    val threshold: Int,

    @SerializedName("field")
    val field: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null
)