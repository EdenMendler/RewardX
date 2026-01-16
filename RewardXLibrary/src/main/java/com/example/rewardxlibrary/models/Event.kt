package com.example.rewardxlibrary.models

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("_id")
    val id: String? = null,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("event_type")
    val eventType: String,

    @SerializedName("event_data")
    val eventData: Map<String, Any>,

    @SerializedName("timestamp")
    val timestamp: String? = null
)