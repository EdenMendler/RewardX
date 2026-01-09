from datetime import datetime
import uuid

class Event:
    @staticmethod
    def create(user_id, event_type, event_data):
        return {
            '_id': str(uuid.uuid4()),
            'user_id': user_id,
            'event_type': event_type,
            'event_data': event_data,
            'timestamp': datetime.now()
        }