from pymongo import MongoClient

class MongoConnection:
    _db = None
    
    @staticmethod
    def initialize(uri):
        try:
            client = MongoClient(uri)
            MongoConnection._db = client.get_database()
        except Exception as e:
            print(f"Failed to connect to MongoDB: {e}")
            MongoConnection._db = None
    
    @staticmethod
    def get_db():
        return MongoConnection._db