import json
import random
import motor
import asyncio
from paho.mqtt import client as mqtt_client

client = motor.motor_asyncio.AsyncIOMotorClient("mongodb+srv://isaquehg:VxeOus9Z6njSPMQk@cluster0.mv5e4bc.mongodb.net/?retryWrites=true&w=majority")
db = client.rightrain

BROKER = 'fbe1817f.ala.us-east-1.emqxsl.com'
PORT = 8883
TOPIC = 'rightrain/data'
CLIENT_ID = f'python-mqtt-{random.randint(0, 1000)}'
USERNAME = 'isaquehg'
PASSWORD = '1arry_3arry'
#ROOT_CA_PATH = '/home/isaquehg/Desktop/right-rain/EMQX/emqxsl-ca.crt'
ROOT_CA_PATH = '/home/ubuntu/right-rain/EMQX/emqxsl-ca.crt'

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)
    # Set Connecting Client ID
    client = mqtt_client.Client(CLIENT_ID)
    # Set CA certificate
    client.tls_set(ca_certs=ROOT_CA_PATH)
    client.username_pw_set(USERNAME, PASSWORD)
    client.on_connect = on_connect
    client.connect(BROKER, PORT)
    return client

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)
    # Set Connecting Client ID
    client = mqtt_client.Client(CLIENT_ID)
    # Set CA certificate
    client.tls_set(ca_certs=ROOT_CA_PATH)
    client.username_pw_set(USERNAME, PASSWORD)
    client.on_connect = on_connect
    client.connect(BROKER, PORT)
    return client

async def subscribe(client: mqtt_client):
    async def on_message(client, userdata, msg):
        # Perform necessary operations with the received data
        payload = msg.payload.decode('utf-8')
        data = json.loads(payload)
        result = await db["devices"].insert_one(data).to_list(length=None)
        print("Document inserted! ID:", result.inserted_id)

    client.subscribe(TOPIC, qos=0)
    client.on_message = await on_message

async def mqtt_subscribe():
    # Set up the MQTT client
    print("function subscribe")
    client = connect_mqtt()
    await subscribe(client)
    client.loop_start()