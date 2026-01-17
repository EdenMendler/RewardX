🏆 RewardX - Achievement & Reward System
A complete, production-ready achievement tracking platform for mobile applications. RewardX makes it easy to add gamification features to your app with minimal integration effort.
🎬 Demo Video
▶️ Watch Demo Video
🔗 Links
Live Portal: https://edenmendler.github.io/RewardX
API Swagger: https://reward-x-bay.vercel.app/apidocs
🚀 Key Features

Real-Time Achievement Tracking: Instantly unlock achievements when users complete actions
Flexible Rule Engine: Support for count, sum, unique days, and threshold conditions
Easy Android Integration: Drop-in SDK with minimal setup
Admin Dashboard: Modern web portal to manage achievements and monitor users
Scalable Architecture: Serverless backend on Vercel with MongoDB Atlas

🎯 Target Audience & Use Cases
RewardX is designed for mobile app developers who want to add engagement and retention features without building a gamification system from scratch.
Ideal for:

Fitness Apps: Track workout streaks, distance milestones, and personal bests
E-commerce Apps: Reward purchases, spending thresholds, and loyalty
Educational Apps: Celebrate learning progress and daily engagement
Social Apps: Recognize user activity, content creation, and community participation

🏗️ System Architecture
Show Image
Flow: Mobile App → SDK → Backend API → Rule Evaluation → Database → Achievements → SDK → App
The system consists of four main components:
1. Backend API
Tech: Python, Flask, MongoDB
Deployment: Vercel (Serverless)
The core service handling users, events, achievements, and real-time rule evaluation.
📖 Backend API Documentation
2. Admin Portal
Tech: React, TailwindCSS
Deployment: GitHub Pages
Modern dashboard for managing achievements, rules, and monitoring user activity.
🎨 Portal Documentation
3. Android SDK
Tech: Kotlin, Retrofit, Coroutines
Distribution: JitPack
Lightweight library that makes integration as simple as 3 lines of code.
📱 Android SDK Documentation
4. Demo App
Tech: Kotlin, Jetpack Compose
Example expense tracker app showcasing real-world SDK integration.
💡 Demo App Documentation
📂 Project Structure
RewardX/
├── Backend-API/          # Flask REST API
├── Reward-Portal/        # React Admin Dashboard  
├── RewardXLibrary/       # Android SDK
└── app/                  # Demo Application
⚡ Quick Start
For App Developers (Integrate the SDK)

Add JitPack repository to settings.gradle.kts:

kotlinmaven { url = uri("https://jitpack.io") }

Add dependency to build.gradle:

kotlinimplementation("com.github.YourUsername:RewardX:v1.0.0")

Initialize and use:

kotlin// In Application class
RewardXSDK.initialize(this, "https://reward-x-bay.vercel.app")

// In your activity
RewardXSDK.setUserId(userId)
RewardXSDK.trackEvent("purchase", mapOf("amount" to 100))
For System Administrators (Run the Portal)
bashcd Reward-Portal
npm install
npm start
For Backend Developers (Deploy Your Own)
bashcd Backend-API
pip install -r requirements.txt
python app.py
📊 How It Works
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
🛠️ Tech Stack
ComponentTechnologiesBackendPython, Flask, MongoDBPortalReact, TailwindCSSSDKKotlin, JitPackDemo AppKotlin, Jetpack Compose
🔐 Security

HTTPS-only API communication
MongoDB connection with authentication
Input validation on all endpoints
CORS configuration for portal
No sensitive data in client SDK

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
