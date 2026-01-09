from flask import request, jsonify, Blueprint
from database.mongo_connection import MongoConnection
from models.user import User

users_bp = Blueprint('users', __name__)

@users_bp.route('/users', methods=['POST'])
def create_user():
    """
    Create a new user
    ---
    tags:
      - Users
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - username
            - email
          properties:
            username:
              type: string
              example: john_doe
            email:
              type: string
              example: john@example.com
    responses:
      201:
        description: User created successfully
      400:
        description: Invalid input
      500:
        description: Database error
    """
    data = request.json
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    if not all(key in data for key in ['username', 'email']):
        return jsonify({'error': 'Invalid input!'}), 400
    
    user = User.create(data['username'], data['email'])
    
    users_collection = db['users']
    users_collection.insert_one(user)
    
    return jsonify({'message': 'User created successfully!', '_id': user['_id']}), 201

@users_bp.route('/users', methods=['GET'])
def get_all_users():
    """
    Get all users
    ---
    tags:
      - Users
    responses:
      200:
        description: List of all users
      500:
        description: Database error
    """
    all_users = []
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    users_collection = db['users']
    
    for user in users_collection.find():
        all_users.append(user)
    
    return jsonify(all_users), 200

@users_bp.route('/users/<user_id>', methods=['GET'])
def get_user(user_id):
    """
    Get user by ID
    ---
    tags:
      - Users
    parameters:
      - name: user_id
        in: path
        type: string
        required: true
        description: User ID
    responses:
      200:
        description: User details
      404:
        description: User not found
      500:
        description: Database error
    """
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    users_collection = db['users']
    user = users_collection.find_one({'_id': user_id})
    
    if user is None:
        return jsonify({'error': 'User not found'}), 404
    
    return jsonify(user), 200

@users_bp.route('/users/<user_id>/achievements', methods=['GET'])
def get_user_achievements(user_id):
    """
    Get all achievements for a user
    ---
    tags:
      - Users
    parameters:
      - name: user_id
        in: path
        type: string
        required: true
        description: User ID
    responses:
      200:
        description: List of user achievements
      404:
        description: User not found
      500:
        description: Database error
    """
    all_achievements = []
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    users_collection = db['users']
    user = users_collection.find_one({'_id': user_id})
    
    if user is None:
        return jsonify({'error': 'User not found'}), 404
    
    achievement_ids = user.get('achievements', [])
    achievements_collection = db['achievements']
    
    for achievement_id in achievement_ids:
        achievement = achievements_collection.find_one({'_id': achievement_id})
        if achievement:
            all_achievements.append(achievement)
    
    return jsonify(all_achievements), 200

@users_bp.route('/users/<user_id>', methods=['DELETE'])
def delete_user(user_id):
    """
    Delete a user
    ---
    tags:
      - Users
    parameters:
      - name: user_id
        in: path
        type: string
        required: true
        description: User ID
    responses:
      200:
        description: User deleted successfully
      404:
        description: User not found
      500:
        description: Database error
    """
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    users_collection = db['users']
    events_collection = db['events']
    
    result = users_collection.delete_one({'_id': user_id})
    
    if result.deleted_count == 0:
        return jsonify({'error': 'User not found'}), 404
    
    # Delete all user events
    events_collection.delete_many({'user_id': user_id})
    
    return jsonify({'message': 'User deleted successfully'}), 200