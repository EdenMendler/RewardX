# 📱 RewardX Android SDK

An easy-to-integrate Kotlin library for adding achievement tracking to your Android app.

## 🚀 Installation

### Step 1: Add JitPack Repository

Add this to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2: Add Dependency

Add to your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.YourUsername:RewardX:v1.0.0")
}
```

## 💡 Quick Start

### 1. Initialize the SDK

Initialize in your `Application` class:

```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize with your backend URL
        RewardXSDK.initialize(
            context = this,
            baseUrl = "https://reward-x-bay.vercel.app"
        )
    }
}
```

Don't forget to register your Application class in `AndroidManifest.xml`:

```xml
<application
    android:name=".MyApp"
    ...>
```

### 2. Set User ID

Set the current user before tracking events:

```kotlin
// After user logs in
RewardXSDK.setUserId("user_12345")
```

### 3. Track Events

Track user actions throughout your app:

```kotlin
// Simple event
RewardXSDK.trackEvent("level_completed")

// Event with data
RewardXSDK.trackEvent("purchase", mapOf(
    "amount" to 99.99,
    "category" to "premium"
))

// Event with callback
RewardXSDK.trackEvent("daily_login") { result ->
    result.onSuccess { achievements ->
        // Handle newly unlocked achievements
        achievements.forEach { achievement ->
            showAchievementNotification(achievement)
        }
    }.onFailure { error ->
        Log.e("RewardX", "Failed to track event: ${error.message}")
    }
}
```

### 4. Listen for Achievements

Implement the listener to respond when achievements are unlocked:

```kotlin
class MainActivity : AppCompatActivity() {
    
    private val achievementListener = object : RewardXSDK.AchievementListener {
        override fun onAchievementsUnlocked(achievements: List<Achievement>) {
            achievements.forEach { achievement ->
                // Show notification, confetti, sound effects, etc.
                showAchievementDialog(achievement)
            }
        }
    }
    
    override fun onStart() {
        super.onStart()
        RewardXSDK.setAchievementListener(achievementListener)
    }
    
    override fun onStop() {
        super.onStop()
        RewardXSDK.removeAchievementListener()
    }
}
```

## 🎯 Core API Reference

### RewardXSDK

#### Initialize
```kotlin
RewardXSDK.initialize(context: Context, baseUrl: String)
```
Must be called before any other SDK methods. Typically in `Application.onCreate()`.

#### Set User ID
```kotlin
RewardXSDK.setUserId(userId: String)
```
Identifies the current user. Required before tracking events.

#### Track Event
```kotlin
// Without callback
RewardXSDK.trackEvent(eventType: String, eventData: Map<String, Any> = emptyMap())

// With callback
RewardXSDK.trackEvent(
    eventType: String, 
    eventData: Map<String, Any> = emptyMap(),
    callback: (Result<List<Achievement>>) -> Unit
)
```

#### Get User Info
```kotlin
RewardXSDK.getUser(userId: String, callback: (Result<User>) -> Unit)
```

Returns:
```kotlin
data class User(
    val id: String,
    val username: String,
    val email: String,
    val totalPoints: Int,
    val achievements: List<String>
)
```

#### Get User Achievements
```kotlin
RewardXSDK.getUserAchievements(userId: String, callback: (Result<List<Achievement>>) -> Unit)
```

Returns:
```kotlin
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val points: Int,
    val icon: String
)
```

#### Set Achievement Listener
```kotlin
RewardXSDK.setAchievementListener(listener: AchievementListener)
```

Listener interface:
```kotlin
interface AchievementListener {
    fun onAchievementsUnlocked(achievements: List<Achievement>)
}
```

## 📝 Usage Examples

### Example 1: E-commerce App

```kotlin
// User makes a purchase
fun onPurchaseComplete(amount: Double, itemCount: Int) {
    RewardXSDK.trackEvent("purchase", mapOf(
        "amount" to amount,
        "items" to itemCount,
        "category" to "electronics"
    )) { result ->
        result.onSuccess { newAchievements ->
            if (newAchievements.isNotEmpty()) {
                // "Big Spender" achievement unlocked!
                showCelebration(newAchievements)
            }
        }
    }
}
```

### Example 2: Fitness App

```kotlin
// User completes a workout
fun onWorkoutComplete(distance: Double, duration: Int) {
    RewardXSDK.trackEvent("workout_completed", mapOf(
        "distance_km" to distance,
        "duration_minutes" to duration,
        "type" to "running"
    ))
}

// User opens app daily
fun onAppOpened() {
    RewardXSDK.trackEvent("daily_login")
}
```

### Example 3: Social App

```kotlin
// User posts content
fun onPostCreated(postType: String) {
    RewardXSDK.trackEvent("post_created", mapOf(
        "type" to postType
    ))
}

// User receives likes
fun onLikeReceived(totalLikes: Int) {
    RewardXSDK.trackEvent("like_received", mapOf(
        "total_likes" to totalLikes
    ))
}
```

## 📦 Dependencies

The SDK includes these dependencies (automatically handled by Gradle):

```kotlin
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.11.0")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```
