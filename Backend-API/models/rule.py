from datetime import datetime
import uuid

class Rule:
    @staticmethod
    def create(achievement_id, event_type, condition_type, threshold, field=None, category=None):
        rule = {
            '_id': str(uuid.uuid4()),
            'achievement_id': achievement_id,
            'event_type': event_type,
            'condition_type': condition_type,
            'threshold': threshold,
            'field': field,
            'created_at': datetime.now()
        }
        
        # Add category only if provided
        if category is not None:
            rule['category'] = category
            
        return rule
