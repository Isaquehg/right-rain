'''
export MONGODB_URL="mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority"
'''

import asyncio
from datetime import timedelta
import datetime
from fastapi import Depends, FastAPI, HTTPException
from fastapi.security import OAuth2PasswordBearer
from pydantic import BaseModel, Field
from typing import Optional, List, Union
import motor.motor_asyncio
from typing import List, Dict
import jwt
from passlib.context import CryptContext
import uvicorn
import auth
from mqtt import mqtt_subscribe

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
    d_id: Optional[str] = Field(None)
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
    id: str = Field(...)
    name: str = Field(..., min_length=1, max_length=100)
    email: str = Field(..., min_length=5, max_length=100, 
                       regex=r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$")
    password: str = Field(..., min_length=8)
    number: str = Field(..., regex=r"^\d{9,15}$")

class HistoryDataPoint(BaseModel):
    timestamp: str
    value: Union[int, float]

class HistoryData(BaseModel):
    data: List[HistoryDataPoint]

# -------------------------------------------ROUTES----------------------------------------------------
@app.on_event("startup")
async def startup_event():
    # Start the MQTT subscription in a separate task
    #asyncio.create_task(mqtt_subscribe())
    print("heree")

@app.route("/")
async def root():
    return {"hello": "world"}

# Authenticate Login with JWT
@app.post("/token")
async def login(form_data: OAuth2PasswordRequestFormCustom):
    user = await auth.authenticate_user(form_data.username, form_data.password, db)
    if not user:
        raise HTTPException(status_code=401, detail="Invalid Credentials")

    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = await auth.create_access_token(
        data={"sub": user["email"]}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}

# User's Home Screen with authentication
@app.get("/home/{u_id}", response_description="List all devices", response_model=List[DeviceData])
async def get_user_data(u_id: str, token: str = Depends(oauth2_scheme)):
    try:
        #payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        #username = payload.get("sub")

        devices = []
        async for device in db["devices"].find({"u_id": u_id}):
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
    
# Retrieve sensor's history
@app.get("/home/{u_id}/{d_id}/{sensor}", response_description="List sensor's history", response_model=HistoryData)
async def get_sensor_history(u_id: str, d_id: str, sensor: str, start_date: str, end_date: str, token: str = Depends(oauth2_scheme)):
    try:
        # Converting incoming dates
        print("before iso conversion")
        start_date_iso = convert_to_iso_date(start_date)
        end_date_iso = convert_to_iso_date(end_date)
        print("before query")
        # Query with filters
        query = {
            "u_id": u_id,
            "d_id": d_id,
            sensor: {"$exists": True},
            "date": {
                "$gte": start_date_iso,
                "$lte": end_date_iso
            }
        }

        device = await db["devices"].find(query)
        print(history)
        if device:
            # Create a list of HistoryDataPoint objects
            history_data = [
                HistoryDataPoint(timestamp=str(data["date"]), value=data[sensor])
                for data in device
            ]
            
            # Create a HistoryData object with the history_data list
            history = HistoryData(data=history_data)
            return history

        raise HTTPException(status_code=404, detail=f"Device's sensor with ID {d_id} not found")
    
    except:
        raise HTTPException(status_code=401, detail="Invalid Credentials")

# Function to convert date types
def convert_to_iso_date(date_str):
    # Converting date to datetime object
    date_obj = datetime.strptime(date_str, "%d/%m/%Y")
    
    # Converting datetime object to ISO format
    iso_date_str = date_obj.isoformat()
    
    return iso_date_str

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
