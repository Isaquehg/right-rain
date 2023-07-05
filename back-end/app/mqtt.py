import asyncio
from aiomqtt import Client as mqtt_client
import motor.motor_asyncio
import json
import random

client = motor.motor_asyncio.AsyncIOMotorClient("mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority")
db = client.rightrain
result = None

BROKER = 'fbe1817f.ala.us-east-1.emqxsl.com'
PORT = 8883
TOPIC = 'rightrain/data'
CLIENT_ID = f'python-mqtt-{random.randint(0, 1000)}'
USERNAME = 'isaquehg'
PASSWORD = '1arry_3arry'
ROOT_CA_PATH = '/home/ubuntu/right-rain/EMQX/emqxsl-ca.crt'

async def save_to_db(data):
    result = await db["devices"].insert_one(data)
    print("Document inserted! ID:", result.inserted_id)

async def on_message(topic, payload, qos, retain):
    data = json.loads(payload.decode('utf-8'))
    await save_to_db(data)

async def subscribe(client):
    await client.connect(BROKER, PORT, ssl=True, username=USERNAME, password=PASSWORD)
    await client.subscribe(TOPIC, qos=0)
    async with client.unfiltered_messages() as messages:
        async for message in messages:
            await on_message(message.topic, message.payload, message.qos, message.retain)

async def mqtt_subscribe():
    client = mqtt_client(client_id=CLIENT_ID)
    await subscribe(client)
