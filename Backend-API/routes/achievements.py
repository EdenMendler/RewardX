from flask import request, jsonify, Blueprint
from database.mongo_connection import MongoConnection
from models.achievement import Achievement

achievements_bp = Blueprint('achievements', __name__)

@achievements_bp.route('/achievements', methods=['POST'])
def create_achievement():
    """
    Create a new achievement
    ---
    tags:
      - Achievements
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - name
            - description
            - points
            - icon
          properties:
            name:
              type: string
              example: First Expense
            description:
              type: string
              example: Add your first expense
            points:
              type: integer
              example: 10
            icon:
              type: string
              example: 💰
    responses:
      201:
        description: Achievement created successfully
      400:
        description: Invalid input
      500:
        description: Database error
    """
    data = request.json
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    if not all(key in data for key in ['name', 'description', 'points', 'icon']):
        return jsonify({'error': 'Invalid input!'}), 400
    
    achievement = Achievement.create(
        data['name'],
        data['description'],
        data['points'],
        data['icon']
    )
    
    achievements_collection = db['achievements']
    achievements_collection.insert_one(achievement)
    
    return jsonify({'message': 'Achievement created successfully!', '_id': achievement['_id']}), 201

@achievements_bp.route('/achievements', methods=['GET'])
def get_all_achievements():
    """
    Get all achievements
    ---
    tags:
      - Achievements
    responses:
      200:
        description: List of all achievements
      500:
        description: Database error
    """
    all_achievements = []
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    achievements_collection = db['achievements']
    
    for achievement in achievements_collection.find():
        all_achievements.append(achievement)
    
    return jsonify(all_achievements), 200

@achievements_bp.route('/achievements/<achievement_id>', methods=['GET'])
def get_achievement(achievement_id):
    """
    Get achievement by ID
    ---
    tags:
      - Achievements
    parameters:
      - name: achievement_id
        in: path
        type: string
        required: true
        description: Achievement ID
    responses:
      200:
        description: Achievement details
      404:
        description: Achievement not found
      500:
        description: Database error
    """
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    achievements_collection = db['achievements']
    achievement = achievements_collection.find_one({'_id': achievement_id})
    
    if achievement is None:
        return jsonify({'error': 'Achievement not found'}), 404
    
    return jsonify(achievement), 200

@achievements_bp.route('/achievements/<achievement_id>', methods=['DELETE'])
def delete_achievement(achievement_id):
    """
    Delete an achievement
    ---
    tags:
      - Achievements
    parameters:
      - name: achievement_id
        in: path
        type: string
        required: true
        description: Achievement ID
    responses:
      200:
        description: Achievement deleted successfully
      404:
        description: Achievement not found
      500:
        description: Database error
    """
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    achievements_collection = db['achievements']
    rules_collection = db['rules']
    
    result = achievements_collection.delete_one({'_id': achievement_id})
    
    if result.deleted_count == 0:
        return jsonify({'error': 'Achievement not found'}), 404
    
    rules_collection.delete_many({'achievement_id': achievement_id})
    
    return jsonify({'message': 'Achievement deleted successfully'}), 200