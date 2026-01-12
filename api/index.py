import sys
import os

# Add Backend-API to Python path
backend_path = os.path.join(os.path.dirname(__file__), '..', 'Backend-API')
sys.path.insert(0, backend_path)

# Import Flask app
from app import app

# This is what Vercel calls
application = app