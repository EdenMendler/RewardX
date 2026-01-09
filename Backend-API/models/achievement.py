from datetime import datetime
import uuid

class Achievement:
    @staticmethod
    def create(name, description, points, icon):
        return {
            '_id': str(uuid.uuid4()),
            'name': name,
            'description': description,
            'points': points,
            'icon': icon,
            'created_at': datetime.now()
        }