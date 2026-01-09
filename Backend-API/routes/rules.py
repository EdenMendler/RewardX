from flask import request, jsonify, Blueprint
from database.mongo_connection import MongoConnection
from models.rule import Rule

rules_bp = Blueprint('rules', __name__)

@rules_bp.route('/rules', methods=['POST'])
def create_rule():
    """
    Create a new achievement rule
    ---
    tags:
      - Rules
    parameters:
      - name: body
        in: body
        required: true
        schema:
          type: object
          required:
            - achievement_id
            - event_type
            - condition_type
            - threshold
          properties:
            achievement_id:
              type: string
              example: achievement-uuid-here
            event_type:
              type: string
              example: expense_added
            condition_type:
              type: string
              example: count
              enum: [count, sum, unique_days]
            threshold:
              type: integer
              example: 5
            field:
              type: string
              example: amount
    responses:
      201:
        description: Rule created successfully
      400:
        description: Invalid input
      500:
        description: Database error
    """
    data = request.json
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    required_fields = ['achievement_id', 'event_type', 'condition_type', 'threshold']
    if not all(key in data for key in required_fields):
        return jsonify({'error': 'Invalid input!'}), 400
    
    rule = Rule.create(
        data['achievement_id'],
        data['event_type'],
        data['condition_type'],
        data['threshold'],
        data.get('field')
    )
    
    rules_collection = db['rules']
    rules_collection.insert_one(rule)
    
    return jsonify({'message': 'Rule created successfully!', '_id': rule['_id']}), 201

@rules_bp.route('/rules', methods=['GET'])
def get_all_rules():
    """
    Get all rules
    ---
    tags:
      - Rules
    responses:
      200:
        description: List of all rules
      500:
        description: Database error
    """
    all_rules = []
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    rules_collection = db['rules']
    
    for rule in rules_collection.find():
        all_rules.append(rule)
    
    return jsonify(all_rules), 200

@rules_bp.route('/rules/<rule_id>', methods=['DELETE'])
def delete_rule(rule_id):
    """
    Delete a rule
    ---
    tags:
      - Rules
    parameters:
      - name: rule_id
        in: path
        type: string
        required: true
        description: Rule ID
    responses:
      200:
        description: Rule deleted successfully
      404:
        description: Rule not found
      500:
        description: Database error
    """
    db = MongoConnection.get_db()
    
    if db is None:
        return jsonify({'error': 'Could not connect to the database'}), 500
    
    rules_collection = db['rules']
    result = rules_collection.delete_one({'_id': rule_id})
    
    if result.deleted_count == 0:
        return jsonify({'error': 'Rule not found'}), 404
    
    return jsonify({'message': 'Rule deleted successfully'}), 200