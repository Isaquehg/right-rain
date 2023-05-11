#ROUTES
#/login
#/register
#/home/{id}
#/home/{id}/{id_loc}
#home/{id}/{id_loc}/pluviosity
#home/{id}/{id_loc}/soilhumidity
#home/{id}/{id_loc}/airhumidity
#home/{id}/{id_loc}/temperature
#home/{id}/{id_loc}/wind-dir
#home/{id}/{id_loc}/wind-vel
#home/{id}/{id_loc}/luminosity
#home/{id}/{id_loc}/pression
#home/{id}/{id_loc}/rain-det

import os
from fastapi import FastAPI, Body, HTTPException, status
from fastapi.responses import Response, JSONResponse
from fastapi.encoders import jsonable_encoder
from pydantic import BaseModel, Field
from bson.objectid import ObjectId
from typing import Optional, List
import motor.motor_asyncio
from typing import List, Dict

## Set the environment variables for the certificate and private key paths
#export DEVICE_CERT_PATH="/path/to/device/cert.pem"
#export DEVICE_KEY_PATH="/path/to/device/key.pem"
#export MONGODB_URL="mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority"

app = FastAPI()
client = motor.motor_asyncio.AsyncIOMotorClient(os.environ["MONGODB_URL"])
db = client.rightrain

# converting _id BSON to string
class PyObjectId(ObjectId):
    @classmethod
    def __get_validators__(cls):
        yield cls.validate

    @classmethod
    def validate(cls, v):
        if not ObjectId.is_valid(v):
            raise ValueError("Invalid objectid")
        return ObjectId(v)

    @classmethod
    def __modify_schema__(cls, field_schema):
        field_schema.update(type="string")

class DeviceData(BaseModel):
    u_first_name: str = Field(..., min_length=1, max_length=30)
    latitude: Optional[float] = Field(None)
    longitude: Optional[float] = Field(None)
    date: Optional[str] = Field(None, regex=r"^\d{2}-\d{2}-\d{4}T\d{2}:\d{2}:\d{2}$")
    temperature: Optional[float] = Field(None, ge=-100, le=100)
    air_humidity: Optional[int] = Field(None, ge=0, le=100)
    pluviosity: Optional[int] = Field(None, ge=0, le=10000)
    soil_humidity: Optional[int] = Field(None, ge=0, le=100)
    at_pressure: Optional[int] = Field(None, ge=0)
    wind_vel: Optional[float] = Field(None, ge=0, le = 1000)
    wind_dir: Optional[str] = Field(None, length=1, regex=r"^[nswe]$")
    luminosity: Optional[int] = Field(None, ge=0)
    rain: Optional[bool] = Field(None)
    soil_ph: Optional[float] = Field(None, ge=0, le=14)

class UserData(BaseModel):
    u_first_name: str = Field(..., min_length=1, max_length=30)
    u_last_name: str = Field(..., min_length=1, max_length=100)
    u_email: str = Field(..., min_length=5, max_length=100, 
                       regex=r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$")
    u_password: str = Field(..., min_length=8)
    u_number: str = Field(..., regex=r"^\d{9,15}$")

# MongoDB document keys
USER_KEYS = []
LOCATION_KEYS = ["name", "email", "password", "number", "locations", "latitude", "longitude", "date", "temperature", "air_humidity", "pluviosity", "soil_humidity", "soil_ph", "at_pressure", "wind_vel", "wind_dir", "luminosity", "rain"]

# DEVICE CRUD
# Create Device
'''@app.post("/register", response_description="Add new user", response_model=UserData)
async def create_user(user: UserData = Body(...)):
    user = jsonable_encoder(user)
    new_user = await db["users"].insert_one(user)
    created_user = await db["users"].find_one({"_id": new_user.inserted_id})
    return JSONResponse(status_code=status.HTTP_201_CREATED, content=created_user)
'''
# -> implement auth
# User's Home Screen
@app.get("/home/{name}", response_description="List all devices", response_model=DeviceData)
async def show_user(name: str):
    if (device := await db["devices"].find_one({"u_first_name": name})) is not None:
        print(type(device))
        return device

    raise HTTPException(status_code=404, detail=f"User {name} not found")

# Send Device Data
@app.post("/home/{name}/{device}", response_description="Send device data to specific device", response_model=DeviceData)
async def update_device(name: str, device_name: str, data: DeviceData = Body(...)):
    device = {k: v for k, v in device.dict().items() if v is not None}

    if len(device) >= 1:
        update_result = await db["devices"].update_one({"u_first_name": name}, {"$set": data})

        if update_result.modified_count == 1:
            if (
                updated_device := await db["devices"].find_one({"u_first_name": name})
            ) is not None:
                return updated_device

    if (existing_device := await db["devices"].find_one({"u_first_name": name})) is not None:
        return existing_device

    raise HTTPException(status_code=404, detail=f"User {name} not found")

# Delete Device
@app.delete("/home/{name}", response_description="Delete a device")
async def delete_device(name: str):
    delete_result = await db["devices"].delete_one({"u_first_name": name})

    if delete_result.deleted_count == 1:
        return Response(status_code=status.HTTP_204_NO_CONTENT)

    raise HTTPException(status_code=404, detail=f"User {name} not found")


#LOCATION CRUD
#register new location
#patches the user document with new locations, instead of put, that replaces the entire data
'''
@app.patch("/locations/{user_id}", response_description="Add a new location to a user", response_model=UserData)
async def add_location(user_id: str, location: DeviceData = Body(...)):
    user = await db['users'].find_one({"_id": ObjectId(user_id)})
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    # Generate _id_loc for the new location
    location_dict = location.dict(by_alias=True)
    location_dict["_id_loc"] = ObjectId()
    # Add the new location to the user's list of locations
    db['users'].update_one({"_id": ObjectId(user_id)}, {"$push": {"locations": location_dict}})
    updated_user = await db['users'].find_one({"_id": ObjectId(user_id)})
    return updated_user
    
# get location details
@app.get("/home/{id}/{id_loc}", response_description="List location details", response_model=DeviceData)
async def show_user(id: str):
    if (location := await db["users"].find_one({"_id_loc": id})) is not None:
        return location

    raise HTTPException(status_code=404, detail=f"Location {id} not found")

# update/add location details
@app.put("/home/{id}/addlocation", response_description="Update location details", response_model=UserData)
async def update_user(id: str, location: DeviceData = Body(...)):
    location = {k: v for k, v in location.dict().items() if v is not None}

    #if the request has items
    if len(location) >= 1:
        user_updated_location = UserData.add_location(location)
        update_result = await db["users"].update_one({"_id": id}, {"$set": user_updated_location})

        if update_result.modified_count == 1:
            if (updated_user := await db["users"].find_one({"_id": id})) is not None:
                return updated_user

    if (existing_user := await db["users"].find_one({"_id": id})) is not None:
        return existing_user

    raise HTTPException(status_code=404, detail=f"Location {id} not found")

#delete location
@app.delete("/home/{id}/{id_loc}", response_description="Delete a location")
async def delete_user(id: str):
    delete_result = await db["users"].delete_one({"_id": id})

    if delete_result.deleted_count == 1:
        return Response(status_code=status.HTTP_204_NO_CONTENT)

    raise HTTPException(status_code=404, detail=f"Location {id} not found")
'''