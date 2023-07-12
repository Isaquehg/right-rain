'''
export MONGODB_URL="mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority"
'''

from datetime import timedelta
from bson import ObjectId
import datetime
from fastapi import Depends, FastAPI, HTTPException
from fastapi.security import OAuth2PasswordBearer
from pydantic import BaseModel, Field
from typing import Optional, List, Union
import motor.motor_asyncio
from passlib.context import CryptContext
import uvicorn
import auth

app = FastAPI()
client = motor.motor_asyncio.AsyncIOMotorClient("mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority")
db = client.rightrain

# -------------------------------------------CONSTANTS----------------------------------------------------
USER_KEYS = ["name", "email", "password", "number"]
LOCATION_KEYS = ["id", "locations", "latitude", "longitude", "date", "temperature", "air_humidity", "pluviosity", "soil_humidity", "soil_ph", "at_pressure", "wind_vel", "wind_dir", "luminosity", "rain"]
SECRET_KEY = "seu_secret_key"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 50

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/token")
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

# -------------------------------------------SCHEMES------------------------------------------------------
class OAuth2PasswordRequestFormCustom(BaseModel):
    username: str
    password: str

class DeviceData(BaseModel):
    _id: str = Field(...)
    u_id: str = Field(...)
    d_id: Optional[str] = Field(...)
    d_name: Optional[str] = Field(None)
    latitude: Optional[float] = Field(None)
    longitude: Optional[float] = Field(None)
    date: Optional[str] = Field(None, regex=r"^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}$")
    temperature: Optional[float] = Field(None, ge=-100, le=100)
    air_humidity: Optional[int] = Field(None, ge=0, le=100)
    rainfall: Optional[int] = Field(None, ge=0, le=10000)
    soil_humidity: Optional[int] = Field(None, ge=0, le=100)
    at_pressure: Optional[int] = Field(None, ge=0)
    wind_vel: Optional[float] = Field(None, ge=0, le = 1000)
    wind_dir: Optional[str] = Field(None, length=1, regex=r"^[nswe]$")
    luminosity: Optional[int] = Field(None, ge=0)
    rain: Optional[bool] = Field(None)
    soil_ph: Optional[float] = Field(None, ge=0, le=14)

class UserData(BaseModel):
    id: Optional[str] = Field(None)
    name: str = Field(..., min_length=1, max_length=100)
    email: str = Field(..., min_length=5, max_length=100)
    password: str = Field(..., min_length=5)
    number: str = Field(...)

class HistoryDataPoint(BaseModel):
    timestamp: str
    value: Union[int, float]

class HistoryData(BaseModel):
    data: List[HistoryDataPoint]

# -------------------------------------------ROUTES----------------------------------------------------
@app.route("/")
async def root():
    return {"right": "rain"}

# Authenticate Login with JWT
@app.post("/token")
async def login(form_data: OAuth2PasswordRequestFormCustom):
    user = await auth.authenticate_user(form_data.username, form_data.password, db)
    u_id = str(user["_id"])
    name = str(user["name"])
    if not user:
        raise HTTPException(status_code=401, detail="Invalid Credentials")

    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = await auth.create_access_token(
        data={"sub": user["email"]}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer", "u_id": u_id, "name": name}

@app.post("/register")
async def register_user(user_data: UserData):
    try:
        # Check if user already exists
        existing_user = await db["users"].find_one({"email": str(user_data.email)})
        if existing_user:
            raise HTTPException(status_code=400, detail="User already exists")

        # Create a new user
        user_data_dict = user_data.dict()
        user_data_dict.pop("id", None)
        print(f"User data dict: {user_data_dict}")
        user_id = await db["users"].insert_one(user_data_dict)
        print(f"_id: {user_id}")
        
        # Return the newly created user
        return {
            "message": "User successfully created",
            "user": {
                **user_data_dict
            }
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail="Internal Server Error")

# Return all user's devices (If there are more than one with the same d_id, will return the last one)
@app.get("/home/{u_id}", response_description="List all devices", response_model=List[DeviceData])
async def get_user_data(u_id: str, token: str = Depends(oauth2_scheme)):
    try:
        devices = []
        pipeline = [
            {"$match": {"u_id": u_id}},
            {"$group": {"_id": "$d_id", "device": {"$last": "$$ROOT"}}},
            {"$replaceRoot": {"newRoot": "$device"}}
        ]

        async for device in db["devices"].aggregate(pipeline):
            devices.append(device)

        if devices:
            return devices

        raise HTTPException(status_code=404, detail=f"User's devices with ID {u_id} not found")
    
    except:
        raise HTTPException(status_code=401, detail="Invalid Credentials")

# Show device's sensors
@app.get("/home/{u_id}/{d_id}", response_description="List device's sensors", response_model=DeviceData)
async def get_devices_sensors(u_id: str, d_id: str, token: str = Depends(oauth2_scheme)):
    try:    
        # Query with filters
        query = {
            "u_id": u_id,
            "d_id": d_id,
        }

        device = await db["devices"].find(query)
        print(device)
        if device:
            return device

        raise HTTPException(status_code=404, detail=f"Device's sensor with ID {d_id} not found")
    
    except:
        raise HTTPException(status_code=401, detail="Invalid Credentials")


def convert_to_iso_date(date_str):
    # Converting date to datetime object
    print("entered function")
    date_obj = datetime.strptime(date_str, "%d-%m-%Y")
    
    # Converting datetime object to ISO format
    iso_date_str = date_obj.isoformat()
    print(f"converted: {iso_date_str}")
    return iso_date_str

# Retrieve sensor's history
@app.get("/home/{u_id}/{d_id}/{sensor}", response_description="List sensor's history", response_model=HistoryData)
async def get_sensor_history(u_id: str, d_id: str, sensor: str, start_date: str, end_date: str, token: str = Depends(oauth2_scheme)):
    try:
        # Query with filters
        query = {
            "u_id": u_id,
            "d_id": d_id,
            sensor: {"$exists": True},
            "date": {
                "$gte": start_date,
                "$lte": end_date
            }
        }
        history = await db["devices"].find(query).to_list(length=None)
        if history:
            # Create a list of HistoryDataPoint objects
            history_data = [
                HistoryDataPoint(timestamp=str(data["date"]), value=data[sensor])
                for data in history
            ]
            
            # Create a HistoryData object with the history_data list
            history = HistoryData(data=history_data)
            return history

        raise HTTPException(status_code=404, detail=f"Device's sensor with ID {d_id} not found")
    
    except Exception as e:
        print(f"Error: {str(e)}")
        raise HTTPException(status_code=500, detail="Internal Server Error")


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
