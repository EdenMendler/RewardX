package com.example.rewardxlibrary.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.rewardxlibrary.client.RewardXClient
import com.example.rewardxlibrary.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("unused")
object RewardXSDK {

    private const val PREFS_NAME = "RewardXSDK"
    private const val KEY_USER_ID = "current_user_id"
    private lateinit var prefs: SharedPreferences

    private var currentUserId: String? = null
    private var eventListener: RewardXEventListener? = null

    fun initialize(context: Context, baseUrl: String, debug: Boolean = false) {
        this.prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        RewardXClient.initialize(baseUrl, debug)
        currentUserId = prefs.getString(KEY_USER_ID, null)
    }

    fun setUserId(userId: String) {
        currentUserId = userId
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? = currentUserId

    fun clearUser() {
        currentUserId = null
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    fun setEventListener(listener: RewardXEventListener) {
        this.eventListener = listener
    }

    fun removeEventListener() {
        this.eventListener = null
    }

    suspend fun createUser(username: String, email: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreateUserRequest(username, email)
                val response = RewardXClient.api.createUser(request)

                if (response.error != null) {
                    Result.failure(Exception(response.error))
                } else {
                    val user = RewardXClient.api.getUser(response.id!!)
                    setUserId(user.id)
                    Result.success(user)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getCurrentUser(): Result<User> {
        val userId = currentUserId ?: return Result.failure(Exception("No user set"))

        return withContext(Dispatchers.IO) {
            try {
                val user = RewardXClient.api.getUser(userId)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUser(userId: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = RewardXClient.api.getUser(userId)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserByEmail(email: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val user = RewardXClient.api.getUserByEmail(email)
                Result.success(user)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAllUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val users = RewardXClient.api.getAllUsers()
                Result.success(users)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun trackEvent(
        eventType: String,
        eventData: Map<String, Any> = emptyMap()
    ): Result<List<Achievement>> {
        val userId = currentUserId ?: return Result.failure(Exception("No user set"))

        return withContext(Dispatchers.IO) {
            try {
                val request = TrackEventRequest(userId, eventType, eventData)
                val response = RewardXClient.api.trackEvent(request)

                if (response.newAchievements.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        eventListener?.onAchievementsUnlocked(response.newAchievements)
                    }
                }

                Result.success(response.newAchievements)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun trackEvent(
        userId: String,
        eventType: String,
        eventData: Map<String, Any> = emptyMap()
    ): Result<List<Achievement>> {
        return withContext(Dispatchers.IO) {
            try {
                val request = TrackEventRequest(userId, eventType, eventData)
                val response = RewardXClient.api.trackEvent(request)

                if (response.newAchievements.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        eventListener?.onAchievementsUnlocked(response.newAchievements)
                    }
                }

                Result.success(response.newAchievements)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserEvents(): Result<List<Event>> {
        val userId = currentUserId ?: return Result.failure(Exception("No user set"))

        return withContext(Dispatchers.IO) {
            try {
                val events = RewardXClient.api.getUserEvents(userId)
                Result.success(events)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserEvents(userId: String): Result<List<Event>> {
        return withContext(Dispatchers.IO) {
            try {
                val events = RewardXClient.api.getUserEvents(userId)
                Result.success(events)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserAchievements(): Result<List<Achievement>> {
        val userId = currentUserId ?: return Result.failure(Exception("No user set"))

        return withContext(Dispatchers.IO) {
            try {
                val achievements = RewardXClient.api.getUserAchievements(userId)
                Result.success(achievements)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getUserAchievements(userId: String): Result<List<Achievement>> {
        return withContext(Dispatchers.IO) {
            try {
                val achievements = RewardXClient.api.getUserAchievements(userId)
                Result.success(achievements)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAllAchievements(): Result<List<Achievement>> {
        return withContext(Dispatchers.IO) {
            try {
                val achievements = RewardXClient.api.getAllAchievements()
                Result.success(achievements)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAchievement(achievementId: String): Result<Achievement> {
        return withContext(Dispatchers.IO) {
            try {
                val achievement = RewardXClient.api.getAchievement(achievementId)
                Result.success(achievement)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createAchievement(
        name: String,
        description: String,
        points: Int,
        icon: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreateAchievementRequest(name, description, points, icon)
                val response = RewardXClient.api.createAchievement(request)

                if (response.error != null) {
                    Result.failure(Exception(response.error))
                } else {
                    Result.success(response.id!!)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createRule(
        achievementId: String,
        eventType: String,
        conditionType: String,
        threshold: Int,
        field: String? = null
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = CreateRuleRequest(achievementId, eventType, conditionType, threshold, field)
                val response = RewardXClient.api.createRule(request)

                if (response.error != null) {
                    Result.failure(Exception(response.error))
                } else {
                    Result.success(response.id!!)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun getAllRules(): Result<List<Rule>> {
        return withContext(Dispatchers.IO) {
            try {
                val rules = RewardXClient.api.getAllRules()
                Result.success(rules)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteAchievement(achievementId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                RewardXClient.api.deleteAchievement(achievementId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteRule(ruleId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                RewardXClient.api.deleteRule(ruleId)
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteUser(userId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                RewardXClient.api.deleteUser(userId)

                if (userId == currentUserId) {
                    clearUser()
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
interface RewardXEventListener {
    fun onAchievementsUnlocked(achievements: List<Achievement>)
}