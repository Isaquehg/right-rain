import paho.mqtt.client as mqtt_client
import pymongo
import json
import random

client = pymongo.MongoClient("mongodbstring")
db = client.rightrain

BROKER = 'username.emqxsl.com'
PORT = 8883
TOPIC = 'rightrain/data'
CLIENT_ID = f'python-mqtt-{random.randint(0, 1000)}'
USERNAME = 'username'
PASSWORD = 'password'
ROOT_CA_PATH = '/path/to/cert.crt'

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)
    # Set Connecting Client ID
    client = mqtt_client.Client(CLIENT_ID, transport='websockets')
    # Set CA certificate
    client.tls_set(ca_certs=ROOT_CA_PATH)
    client.username_pw_set(USERNAME, PASSWORD)
    client.on_connect = on_connect
    client.connect(BROKER, PORT)
    return client

def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        # Perform necessary operations with the received data
        payload = msg.payload.decode('utf-8')
        data = json.loads(payload)
        result = db["devices"].insert_one(data)
        print("Document inserted! ID:", result.inserted_id)

    client.subscribe(TOPIC, qos=0)
    client.on_message = on_message

def mqtt_subscribe():
    # Set up the MQTT client
    client = connect_mqtt()
    subscribe(client)
    client.loop_forever()

# Run the MQTT subscription
mqtt_subscribe()
