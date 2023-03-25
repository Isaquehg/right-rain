import os
from fastapi import FastAPI, Body, HTTPException, status
from fastapi.responses import Response, JSONResponse
from fastapi.encoders import jsonable_encoder
from pydantic import BaseModel, Field, EmailStr
from bson import ObjectId
from typing import Optional, List
import motor.motor_asyncio
from typing import List, Dict

#ROUTES
#/login
#/register
#/home/{id}
#/home/{id}/{location}
#home/{id}/{location}/pluviosity
#home/{id}/{location}/soilhumidity
#home/{id}/{location}/airhumidity
#home/{id}/{location}/temperature

app = FastAPI()
client = motor.motor_asyncio.AsyncIOMotorClient(os.environ["MONGODB_URL"])
db = client.college

#converting _id BSON to string
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

class LocationData(BaseModel):
    _id_loc: str
    latitude: float
    longitude: float
    dates: List[Dict[str, Dict[str, float]]]

class UserData(BaseModel):
    _id: str
    name: str
    email: str
    password: str
    locations: List[LocationData]


#USER CRUD
#create user
@app.post("/register", response_description="Add new user", response_model=UserData)
async def create_user(user: UserData = Body(...)):
    user = jsonable_encoder(user)
    new_user = await db["users"].insert_one(user)
    created_user = await db["users"].find_one({"_id": new_user.inserted_id})
    return JSONResponse(status_code=status.HTTP_201_CREATED, content=created_user)

#implement auth
#user home screen
@app.get("/home/{id}", response_description="List all locations", response_model=UserData)
async def show_user(id: str):
    if (location := await db["locations"].find_one({"_id": id})) is not None:
        return location

    raise HTTPException(status_code=404, detail=f"User {id} not found")

#update user
@app.put("/home/{id}", response_description="Update a user", response_model=UserData)
async def update_user(id: str, user: UpdateUserData = Body(...)):
    user = {k: v for k, v in user.dict().items() if v is not None}

    if len(user) >= 1:
        update_result = await db["users"].update_one({"_id": id}, {"$set": user})

        if update_result.modified_count == 1:
            if (
                updated_user := await db["users"].find_one({"_id": id})
            ) is not None:
                return updated_user

    if (existing_user := await db["users"].find_one({"_id": id})) is not None:
        return existing_user

    raise HTTPException(status_code=404, detail=f"User {id} not found")

#delete user
@app.delete("/home/{id}", response_description="Delete a user")
async def delete_user(id: str):
    delete_result = await db["users"].delete_one({"_id": id})

    if delete_result.deleted_count == 1:
        return Response(status_code=status.HTTP_204_NO_CONTENT)

    raise HTTPException(status_code=404, detail=f"User {id} not found")


#LOCATION CRUD
#register new location
@app.post("/register"/{id}, response_description="Add new location", response_model=UserData)
async def create_user(user: UserData = Body(...)):
    user = jsonable_encoder(user)
    new_user = await db["locations"].insert_one(user)
    created_user = await db["locations"].find_one({"_id": new_user.inserted_id})
    return JSONResponse(status_code=status.HTTP_201_CREATED, content=created_user)

#get location details
@app.get("/home/{id}/{idloc}", response_description="List location details", response_model=UserData)
async def show_user(id: str):
    if (location := await db["locations"].find_one({"_id": id})) is not None:
        return location

    raise HTTPException(status_code=404, detail=f"Location {id} not found")

#update location details
@app.put("/home/{id}/{idloc}", response_description="Update location details", response_model=UserData)
async def update_user(id: str, user: UpdateUserData = Body(...)):
    user = {k: v for k, v in user.dict().items() if v is not None}

    if len(user) >= 1:
        update_result = await db["locations"].update_one({"_id": id}, {"$set": user})

        if update_result.modified_count == 1:
            if (
                updated_user := await db["locations"].find_one({"_id": id})
            ) is not None:
                return updated_user

    if (existing_user := await db["locations"].find_one({"_id": id})) is not None:
        return existing_user

    raise HTTPException(status_code=404, detail=f"Location {id} not found")

#delete location
@app.delete("/home/{id}/{idloc}", response_description="Delete a user")
async def delete_user(id: str):
    delete_result = await db["users"].delete_one({"_id": id})

    if delete_result.deleted_count == 1:
        return Response(status_code=status.HTTP_204_NO_CONTENT)

    raise HTTPException(status_code=404, detail=f"Location {id} not found")