# 🏆 RewardX - Achievement & Reward System

A complete, production-ready achievement tracking platform for mobile applications. RewardX makes it easy to add gamification features to your app with minimal integration effort.

## 🔗 Links

**Live Portal**: https://edenmendler.github.io/RewardX  
**API Swagger**: https://reward-x-bay.vercel.app/apidocs

## 🚀 Key Features

- **Real-Time Achievement Tracking**: Instantly unlock achievements when users complete actions
- **Flexible Rule Engine**: Support for count, sum, unique days, and threshold conditions
- **Easy Android Integration**: Drop-in SDK with minimal setup
- **Admin Dashboard**: Modern web portal to manage achievements and monitor users
- **Scalable Architecture**: Serverless backend on Vercel with MongoDB Atlas

## 🎯 Target Audience & Use Cases

RewardX is designed for mobile app developers who want to add engagement and retention features without building a gamification system from scratch.

**Ideal for:**
- **Fitness Apps**: Track workout streaks, distance milestones, and personal bests
- **E-commerce Apps**: Reward purchases, spending thresholds, and loyalty
- **Educational Apps**: Celebrate learning progress and daily engagement
- **Social Apps**: Recognize user activity, content creation, and community participation

## 🏗️ System Architecture

```
┌─────────────────┐
│  Android App    │
│   (Your App)    │
└────────┬────────┘
         │ RewardX SDK
         ↓
┌─────────────────┐      ┌──────────────────┐
│  Backend API    │◄────►│  Admin Portal    │
│   (Vercel)      │      │ (GitHub Pages)   │
└────────┬────────┘      └──────────────────┘
         │
         ↓
┌─────────────────┐
│  MongoDB Atlas  │
└─────────────────┘
```

The system consists of four main components:

### 1. Backend API
**Tech**: Python, Flask, MongoDB  
**Deployment**: Vercel (Serverless)

The core service handling users, events, achievements, and real-time rule evaluation.

**Documentation**: https://github.com/EdenMendler/RewardX/blob/main/docs/Backend%20API.md

### 2. Admin Portal
**Tech**: React, TailwindCSS  
**Deployment**: GitHub Pages

Modern dashboard for managing achievements, rules, and monitoring user activity.

**Documentation**: https://github.com/EdenMendler/RewardX/blob/main/docs/Admin%20Portal.md

### 3. Android SDK
**Tech**: Kotlin, Retrofit, Coroutines  
**Distribution**: JitPack

Lightweight library that makes integration as simple as 3 lines of code.

**Documentation**: https://github.com/EdenMendler/RewardX/blob/main/docs/Android%20SDK.md

### 4. Demo App
**Tech**: Kotlin, Jetpack Compose

Example expense tracker app showcasing real-world SDK integration.

**Documentation**: https://github.com/EdenMendler/RewardX/blob/main/docs/Demo%20App.md

## 📂 Project Structure

```
RewardX/
├── Backend-API/          # Flask REST API
├── Reward-Portal/        # React Admin Dashboard  
├── RewardXLibrary/       # Android SDK
└── app/                  # Demo Application
```

## ⚡ Quick Start

### For App Developers (Integrate the SDK)

1. **Add JitPack repository** to `settings.gradle.kts`:
```kotlin
maven { url = uri("https://jitpack.io") }
```

2. **Add dependency** to `build.gradle`:
```kotlin
implementation("com.github.YourUsername:RewardX:v1.0.0")
```

3. **Initialize and use**:
```kotlin
// In Application class
RewardXSDK.initialize(this, "https://reward-x-bay.vercel.app")

// In your activity
RewardXSDK.setUserId(userId)
RewardXSDK.trackEvent("purchase", mapOf("amount" to 100))
```

### For System Administrators (Run the Portal)

```bash
cd Reward-Portal
npm install
npm start
```

### For Backend Developers (Deploy Your Own)

```bash
cd Backend-API
pip install -r requirements.txt
python app.py
```

## 📊 How It Works

```
1. User performs action (e.g., makes purchase)
         ↓
2. App tracks event via SDK
         ↓
3. Backend evaluates achievement rules
         ↓
4. Achievement unlocked? → Award points & notify user
         ↓
5. SDK listener receives new achievements
         ↓
6. App displays achievement badge
```

## 🛠️ Tech Stack

| Component | Technologies |
|-----------|-------------|
| Backend | Python, Flask, MongoDB |
| Portal | React, TailwindCSS |
| SDK | Kotlin, JitPack |
| Demo App | Kotlin, Jetpack Compose |

## 🔐 Security

- HTTPS-only API communication
- MongoDB connection with authentication
- Input validation on all endpoints
- CORS configuration for portal
- No sensitive data in client SDK

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
