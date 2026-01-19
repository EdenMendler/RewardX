from database.mongo_connection import MongoConnection
from datetime import datetime

class AchievementEvaluator:
    
    @staticmethod
    def evaluate(user_id, event):
        """Evaluate if the event triggers any achievements"""
        db = MongoConnection.get_db()
        new_achievements = []
        
        # Get user
        user = db.users.find_one({'_id': user_id})
        if not user:
            return new_achievements
        
        # Find rules matching this event type
        rules = list(db.rules.find({'event_type': event['event_type']}))
        
        for rule in rules:
            achievement_id = rule['achievement_id']
            
            # Skip if user already has this achievement
            if achievement_id in user.get('achievements', []):
                continue
            
            # Evaluate condition
            if AchievementEvaluator._check_condition(user_id, rule, event):
                # Award achievement
                achievement = db.achievements.find_one({'_id': achievement_id})
                if achievement:
                    db.users.update_one(
                        {'_id': user_id},
                        {
                            '$push': {'achievements': achievement_id},
                            '$inc': {'total_points': achievement['points']}
                        }
                    )
                    new_achievements.append(achievement)
        
        return new_achievements
    
    @staticmethod
    def _check_condition(user_id, rule, current_event):
        """Check if a rule condition is met"""
        db = MongoConnection.get_db()
        condition_type = rule['condition_type']
        threshold = rule['threshold']
        
        # Build base query with optional category filter
        base_query = {
            'user_id': user_id,
            'event_type': rule['event_type']
        }
        
        # Add category filter if specified in rule
        if 'category' in rule:
            base_query['event_data.category'] = rule['category']
        
        if condition_type == 'count':
            # Count events of this type
            count = db.events.count_documents(base_query)
            return count >= threshold
        
        elif condition_type == 'sum':
            # Sum a specific field
            field = rule.get('field')
            if not field:
                return False
            
            pipeline = [
                {'$match': base_query},
                {'$group': {
                    '_id': None,
                    'total': {'$sum': f'$event_data.{field}'}
                }}
            ]
            result = list(db.events.aggregate(pipeline))
            total = result[0]['total'] if result else 0
            return total >= threshold
        
        elif condition_type == 'unique_days':
            # Count unique days with events
            pipeline = [
                {'$match': base_query},
                {'$group': {
                    '_id': {
                        '$dateToString': {
                            'format': '%Y-%m-%d',
                            'date': '$timestamp'
                        }
                    }
                }},
                {'$count': 'unique_days'}
            ]
            result = list(db.events.aggregate(pipeline))
            unique_days = result[0]['unique_days'] if result else 0
            return unique_days >= threshold
        
        elif condition_type == 'threshold':
            # Check if current event's field value meets threshold
            field = rule.get('field')
            if not field:
                return False
            
            # Get the field value from current event data
            event_value = current_event.get('event_data', {}).get(field)
            if event_value is None:
                return False
            
            # Support different operators (default is >=)
            operator = rule.get('operator', '>=')
            
            if operator == '>':
                return event_value > threshold
            elif operator == '>=':
                return event_value >= threshold
            elif operator == '<':
                return event_value < threshold
            elif operator == '<=':
                return event_value <= threshold
            elif operator == '==':
                return event_value == threshold
            else:
                return event_value >= threshold
        
        return False
