# 📖 RewardX Backend API

RESTful API service for achievement tracking and gamification.

## 🌐 Deployment

**Swagger UI**: https://reward-x-bay.vercel.app/apidocs  
**Platform**: Vercel (Serverless)  
**Database**: MongoDB Atlas

## 📋 Overview

The Backend API is the core engine of RewardX, handling:
- User account management
- Real-time event tracking
- Achievement rule evaluation
- Point accumulation

## ⚡ Quick Start

### Local Development

```bash
cd Backend-API
pip install -r requirements.txt

# Create .env file
echo "MONGO_URI=your_mongodb_uri" > .env
echo "SECRET_KEY=your_secret_key" >> .env

# Run server
python app.py
```

Server runs on `http://localhost:5000`

### Deploy to Vercel

```bash
# Install Vercel CLI
npm install -g vercel

# Configure environment variables in Vercel dashboard:
# - MONGO_URI
# - SECRET_KEY

# Deploy
vercel deploy --prod
```

## 🧠 Achievement Rule System

The rule engine supports four condition types:

### 1. Count Condition
Triggers after user performs an action N times.

```json
{
    "condition_type": "count",
    "event_type": "level_completed",
    "threshold": 10
}
```

**Example**: "Complete 10 levels"

### 2. Sum Condition
Triggers when accumulated value reaches threshold.

```json
{
    "condition_type": "sum",
    "event_type": "purchase",
    "field": "amount",
    "threshold": 1000
}
```

**Example**: "Spend $1000 total"

### 3. Unique Days Condition
Triggers after performing action on N different days.

```json
{
    "condition_type": "unique_days",
    "event_type": "daily_login",
    "threshold": 7
}
```

**Example**: "Login for 7 days"

### 4. Threshold Condition
Triggers when single event value meets criteria.

```json
{
    "condition_type": "threshold",
    "event_type": "purchase",
    "field": "amount",
    "operator": ">=",
    "threshold": 500
}
```

**Example**: "Make a purchase over $500"

**Supported operators**: `>=`, `>`, `<=`, `<`, `==`

## 🔄 Event Processing Flow

```
1. Client sends event → POST /api/events/track
         ↓
2. Event saved to database
         ↓
3. Achievement Evaluator runs
         ↓
4. For each matching rule:
   - Skip if user already has achievement
   - Check condition (count/sum/unique_days/threshold)
   - If met → Award achievement & points
         ↓
5. Return list of newly unlocked achievements
```

## 🚨 Error Responses

All endpoints return consistent error format:

```json
{
    "error": "Error message description"
}
```

**HTTP Status Codes:**
- `200` - Success
- `201` - Created
- `400` - Bad Request (invalid input)
- `404` - Not Found
- `500` - Server Error

## 🔐 Security

- **Environment Variables**: Secrets stored in `.env` (never committed)
- **MongoDB Authentication**: Strong password + IP whitelist
- **CORS**: Configured to allow Portal origin
- **Input Validation**: All endpoints validate request data
- **HTTPS Only**: TLS encryption for all communication

## 📦 Dependencies

```txt
Flask==3.0.0           # Web framework
pymongo==4.6.0         # MongoDB driver
python-dotenv==1.0.0   # Environment variables
gunicorn==21.2.0       # WSGI server
flasgger==0.9.7.1      # Swagger documentation
flask-cors==4.0.0      # CORS support
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file:

```bash
MONGO_URI=mongodb+srv://username:password@cluster.mongodb.net/rewardx
SECRET_KEY=your-secret-key-here
DEBUG=False
```

### Vercel Configuration

`vercel.json`:
```json
{
  "version": 2,
  "builds": [{
    "src": "api/index.py",
    "use": "@vercel/python"
  }],
  "routes": [{
    "src": "/(.*)",
    "dest": "api/index.py"
  }]
}
```

