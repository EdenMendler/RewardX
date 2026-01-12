import sys
import os

# Add Backend-API to Python path
backend_path = os.path.join(os.path.dirname(__file__), '..', 'Backend-API')
sys.path.insert(0, backend_path)

# Import Flask app
from app import app as application

# Vercel handler
def handler(request):
    return application(request.environ, lambda *args: None)