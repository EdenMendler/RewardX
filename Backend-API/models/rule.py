from datetime import datetime
import uuid

class Rule:
    @staticmethod
    def create(achievement_id, event_type, condition_type, threshold, field=None):
        return {
            '_id': str(uuid.uuid4()),
            'achievement_id': achievement_id,
            'event_type': event_type,
            'condition_type': condition_type,
            'threshold': threshold,
            'field': field,
            'created_at': datetime.now()
        }