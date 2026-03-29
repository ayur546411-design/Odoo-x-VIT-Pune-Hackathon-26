import random
import string
from datetime import datetime, timedelta, timezone
import jwt
from typing import Optional
from .config import settings

def generate_otp(length: int = 6) -> str:
    """Generate a random N-digit OTP."""
    digits = string.digits
    return ''.join(random.choice(digits) for _ in range(length))

def create_access_token(data: dict, expires_delta: Optional[timedelta] = None) -> str:
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.now(timezone.utc) + expires_delta
    else:
        expire = datetime.now(timezone.utc) + timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, settings.JWT_SECRET, algorithm=settings.ALGORITHM)
    return encoded_jwt
