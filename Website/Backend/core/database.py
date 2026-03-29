import motor.motor_asyncio
from pymongo import IndexModel, ASCENDING
from .config import settings

class Database:
    client: motor.motor_asyncio.AsyncIOMotorClient = None
    db = None

db_instance = Database()

async def connect_to_mongo():
    db_instance.client = motor.motor_asyncio.AsyncIOMotorClient(settings.MONGODB_URI)
    db_instance.db = db_instance.client[settings.MONGODB_DB_NAME]
    
    # Create TTL index on Otps collection to auto-delete after 5 minutes (300 seconds)
    otp_collection = db_instance.db.otps
    await otp_collection.create_index(
        [("createdAt", ASCENDING)],
        expireAfterSeconds=300
    )

async def close_mongo_connection():
    if db_instance.client:
        db_instance.client.close()

def get_db():
    return db_instance.db
