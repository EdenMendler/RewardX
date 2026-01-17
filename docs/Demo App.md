# 💡 RewardX Demo App - Income & Expense Tracker

A real-world example application showcasing RewardX SDK integration in an income and expense tracking app.

## 📱 Overview

The Demo App is a fully functional income and expense tracker that demonstrates how to integrate RewardX achievements into a production-quality Android application. It tracks user transactions and rewards them with achievements for financial milestones.

## ✨ Features

### Transaction Tracking
- Add transactions with amount, category, and notes
- Track both income and expenses
- View transaction history
- Categorize transactions (Food, Transport, Shopping, Salary, etc.)
- Monthly overview

### Achievement Integration
- Unlock achievements for spending patterns
- Real-time achievement notifications
- View unlocked achievements in profile
- Track total points earned

### Sample Achievements
- **First Expense**: Log your first transaction (10 points)
- **Big Spender**: Make a purchase over $100 (25 points)
- **Budget Master**: Log 50 total transactions (100 points)

## 🚀 How SDK is Integrated

### 1. Application Initialization

```kotlin
// ExpenseTrackerApp.kt
class ExpenseTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize RewardX SDK
        RewardXSDK.initialize(
            context = this,
            baseUrl = "https://reward-x-bay.vercel.app"
        )
    }
}
```

### 2. Set User ID on Login

```kotlin
// MainActivity.kt - onResume
override fun onResume() {
    super.onResume()
    
    val userId = getUserId() // Get from SharedPreferences or login
    RewardXSDK.setUserId(userId)
}
```

### 3. Track Events When User Actions Occur

```kotlin
// MainViewModel.kt
fun addTransaction(transaction: Transaction) {
    // Save transaction locally
    transactions.add(transaction)
    
    // Track event with RewardX
    RewardXSDK.trackEvent(
        eventType = "transaction_added",
        eventData = mapOf(
            "amount" to transaction.amount,
            "category" to transaction.category
        )
    ) { result ->
        result.onSuccess { newAchievements ->
            // Show achievement dialog
            _newAchievements.value = newAchievements
        }
    }
}
```

### 4. Listen for Achievement Unlocks

```kotlin
// MainActivity.kt
private val achievementListener = object : RewardXSDK.AchievementListener {
    override fun onAchievementsUnlocked(achievements: List<Achievement>) {
        // Show achievement unlock dialog
        achievements.forEach { achievement ->
            AchievementUnlockedDialog(achievement).show(
                supportFragmentManager,
                "achievement"
            )
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
```

### 5. Display User Achievements

```kotlin
// ProfileFragment.kt
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    
    val userId = getUserId()
    
    // Fetch user achievements
    RewardXSDK.getUserAchievements(userId) { result ->
        result.onSuccess { achievements ->
            achievementAdapter.submitList(achievements)
        }
    }
    
    // Fetch user data for points
    RewardXSDK.getUser(userId) { result ->
        result.onSuccess { user ->
            binding.totalPoints.text = "${user.totalPoints} pts"
        }
    }
}
```

## 🎯 Event Tracking Strategy

### Transaction Events

```kotlin
// When user adds a transaction
RewardXSDK.trackEvent("transaction_added", mapOf(
    "amount" to amount,
    "category" to category
))

// When user completes a day of tracking
RewardXSDK.trackEvent("daily_tracking")

// When user reaches spending milestone
if (totalSpent >= 100) {
    RewardXSDK.trackEvent("big_purchase", mapOf(
        "amount" to amount
    ))
}
```

## 🧪 Testing the Integration

### Manual Testing Steps

**First Launch**
- App creates a new user automatically
- User ID is stored in SharedPreferences

### Verifying in Admin Portal

1. Open https://edenmendler.github.io/RewardX
2. Navigate to "Users"
3. Find your test user
4. Verify achievements are showing
5. Navigate to "Events"
6. Filter by your user ID
7. Verify events are being tracked

## 📦 Dependencies

```kotlin
dependencies {
    // RewardX SDK
    implementation("com.github.YourUsername:RewardX:v1.0.0")
    
    // Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Compose (for custom dialogs)
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material:material:1.6.0")
}
```

## 🚀 Running the Demo

1. **Clone Repository**
```bash
git clone https://github.com/EdenMendler/RewardX.git
cd RewardX
```

2. **Open in Android Studio**
   - Open the `app` folder
   - Sync Gradle files

3. **Run**
   - Select an emulator or device
   - Click Run ▶️

4. **Test**
   - Add transactions
   - Watch for achievement unlocks
   - Check profile for earned achievements
