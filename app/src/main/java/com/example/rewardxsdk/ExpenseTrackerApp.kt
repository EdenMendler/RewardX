package com.example.rewardxsdk

import android.app.Application
import com.example.rewardxlibrary.manager.RewardXSDK

class ExpenseTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        RewardXSDK.initialize(
            context = this,
            baseUrl = "https://reward-x-bay.vercel.app/api",
            debug = true
        )
    }
}