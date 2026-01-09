from datetime import datetime
import uuid

class User:
    @staticmethod
    def create(username, email):
        return {
            '_id': str(uuid.uuid4()),
            'username': username,
            'email': email,
            'created_at': datetime.now(),
            'total_points': 0,
            'achievements': []
        }
    
    @staticmethod
    def add_achievement(user, achievement_id, points):
        if 'achievements' not in user:
            user['achievements'] = []
        
        user['achievements'].append(achievement_id)
        user['total_points'] = user.get('total_points', 0) + points
        
        return user