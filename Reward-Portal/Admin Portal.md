# 🎨 RewardX Admin Portal

Modern web dashboard for managing achievements, users, and monitoring system activity.

## 🌐 Live Portal

**URL**: https://edenmendler.github.io/RewardX  
**Tech**: React 18, TailwindCSS, React Router  
**Hosting**: GitHub Pages

## 📋 Overview

The Admin Portal provides a comprehensive interface to manage all aspects of your achievement system without writing code. Perfect for product managers, game designers, and system administrators.

## ✨ Features

### User Management
- View all registered users
- Create new user accounts
- Monitor points and achievements
- Delete users (with cascading cleanup)

### Achievement Management
- Create custom achievements
- Configure name, description, points, and icon
- Edit or delete achievements

### Rule Configuration
- Create unlock conditions for achievements
- Four condition types: Count, Sum, Unique Days, Threshold

### Event Monitoring
- Create events
- Real-time event stream
- Filter by user or event type
- View detailed event data
- Track achievement unlocks

## 🚀 Getting Started

### Option 1: Use the Live Portal

Simply visit https://edenmendler.github.io/RewardX

No installation required!

### Option 2: Run Locally

```bash
cd Reward-Portal
npm install
npm start
```

Portal opens at `http://localhost:3000`

### Option 3: Deploy Your Own

```bash
# Build for production
npm run build

# Deploy to GitHub Pages
npm run deploy
```

## 🔧 Configuration

### Connecting to Your Backend

Edit `src/services/api.js`:

```javascript
const API_BASE_URL = 'https://your-backend.vercel.app/api';
```

### Customizing Appearance

The portal uses TailwindCSS. Customize in `tailwind.config.js`:

```javascript
module.exports = {
  theme: {
    extend: {
      colors: {
        primary: '#3B82F6',    // Change primary color
        secondary: '#8B5CF6',  // Change secondary color
      },
    },
  },
}
```

## 🔐 Security Notes

- No authentication in current version (add if needed)
- Backend handles all data validation
- CORS configured on backend
- Consider adding admin login for production
