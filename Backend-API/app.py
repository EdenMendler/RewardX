from flask import Flask
from flasgger import Swagger
import os
from flask_cors import CORS  
from routes.users import users_bp
from routes.events import events_bp
from routes.achievements import achievements_bp
from routes.rules import rules_bp
from database.mongo_connection import MongoConnection
from config import Config

app = Flask(__name__)
app.config.from_object(Config)
CORS(app)

# Initialize Swagger
swagger = Swagger(app, template={
    "info": {
        "title": "RewardX SDK API",
        "description": "API for tracking user actions and managing achievements",
        "version": "1.0.0"
    },
    "basePath": "/api",
    "schemes": ["https", "http"]
})

# Initialize MongoDB
MongoConnection.initialize(app.config['MONGO_URI'])

# Register blueprints
app.register_blueprint(users_bp, url_prefix='/api')
app.register_blueprint(events_bp, url_prefix='/api')
app.register_blueprint(achievements_bp, url_prefix='/api')
app.register_blueprint(rules_bp, url_prefix='/api')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=int(os.environ.get('PORT', 5000)))