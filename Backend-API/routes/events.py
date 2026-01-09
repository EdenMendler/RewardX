from flask import request, jsonify, Blueprint
from database.mongo_connection import MongoConnection
from services.achievement_evaluator import AchievementEvaluator
from models.event import Event

events_bp = Blueprint('events', __name__)

@events_bp.route('/events', methods=['POST'])
def track_event():
    """
    Track a user event
    ---
    tags:
      - Events
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - user_id
            - event_type
            - event_data
          properties:
            user_id:
              type: string
              example: user-uuid-here
            event_type:
              type: string
              example: expense_added
            event_data:
              type: object
              example: {"amount": 100, "category": "food"}
    responses:
      201:
        description: Event tracked successfully
      400:
        description: Invalid input
      404:
        description: User not found
      500:
        description: Database error
    """
    data = request.json
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    if not all(key in data for key in ['user_id', 'event_type', 'event_data']):
        return jsonify({'error': 'Invalid input!'}), 400
    
    users_collection = db['users']
    user = users_collection.find_one({'_id': data['user_id']})
    
    if user is None:
        return jsonify({'error': 'User not found'}), 404
    
    event = Event.create(data['user_id'], data['event_type'], data['event_data'])
    
    events_collection = db['events']
    events_collection.insert_one(event)
    
    new_achievements = AchievementEvaluator.evaluate(data['user_id'], event)
    
    return jsonify({
        'message': 'Event tracked successfully!',
        '_id': event['_id'],
        'new_achievements': new_achievements
    }), 201

@events_bp.route('/events/<user_id>', methods=['GET'])
def get_user_events(user_id):
    """
    Get all events for a user
    ---
    tags:
      - Events
    parameters:
      - name: user_id
        in: path
        type: string
        required: true
        description: User ID
    responses:
      200:
        description: List of user events
      500:
        description: Database error
    """
    all_events = []
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    events_collection = db['events']
    
    for event in events_collection.find({'user_id': user_id}):
        all_events.append(event)
    
    return jsonify(all_events), 200