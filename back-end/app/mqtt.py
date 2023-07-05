import json
import random
import motor
import asyncio
from asyncio_mqtt import Client as mqtt_client

client = motor.motor_asyncio.AsyncIOMotorClient("mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority")
db = client.rightrain

BROKER = 'fbe1817f.ala.us-east-1.emqxsl.com'
PORT = 8883
TOPIC = 'rightrain/data'
CLIENT_ID = f'python-mqtt-{random.randint(0, 1000)}'
USERNAME = 'isaquehg'
PASSWORD = '1arry_3arry'
ROOT_CA_PATH = '/home/ubuntu/right-rain/EMQX/emqxsl-ca.crt'

async def connect_mqtt() -> mqtt_client:
    client = mqtt_client(client_id=CLIENT_ID)
    await client.connect(BROKER, PORT, username=USERNAME, password=PASSWORD, cafile=ROOT_CA_PATH)
    return client

async def on_message(client, topic, payload, qos, properties):
    data = json.loads(payload)
    await save_to_db(data)

async def save_to_db(data):
    result = await db["devices"].insert_one(data)
    print("Document inserted! ID:", result.inserted_id)

async def mqtt_subscribe():
    client = await connect_mqtt()
    await client.subscribe(TOPIC, qos=0)
    async with client.filtered_messages(TOPIC) as messages:
        async for message in messages:
            await on_message(
                client,
                message.topic,
                message.payload.decode('utf-8'),
                message.qos,
                message.properties,
            )
