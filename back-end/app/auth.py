from datetime import timedelta
import datetime
from fastapi.security import OAuth2PasswordBearer
from passlib.context import CryptContext
import jwt

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/token")
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# Constants
SECRET_KEY = "your_secret_key"
ALGORITHM = "HS256"

# AUTHENTICATION
async def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

async def get_password_hash(password):
    return pwd_context.hash(password)

async def get_user(email: str, db):
    user = await db["users"].find_one({"email": email})
    user["hashed_password"] = await get_password_hash(user["password"])
    return user

async def authenticate_user(email: str, password: str, db):
    user = await get_user(email, db)
    if not user:
        return False
    if not await verify_password(password, user["hashed_password"]):
        return False
    return user

# Generate JWT token
async def create_access_token(data: dict, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt