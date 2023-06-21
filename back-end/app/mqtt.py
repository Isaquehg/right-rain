import asyncio
import json
import logging
import os
import random
from paho.mqtt import client as mqtt_client

BROKER = 'broker.emqx.io'
PORT = 1883
TOPIC = 'rightrain/data'
CLIENT_ID = f'python-mqtt-{random.randint(0, 1000)}'
USERNAME = 'emqx'
PASSWORD = '**********'
DEVICE_CERT = os.environ["DEVICE_CERT"]
PRIVATE_KEY = os.environ["PRIVATE_KEY"]
ROOT_CA = os.environ["ROOT_CA"]

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)
    # Set Connecting Client ID
    client = mqtt_client.Client(CLIENT_ID)
    # Set CA certificate
    client.tls_set(ca_certs='../../EMQX/server-ca.crt')
    client.username_pw_set(USERNAME, PASSWORD)
    client.on_connect = on_connect
    client.connect(BROKER, PORT)
    return client

def on_message(client, userdata, message):
    print("on_message function")
    payload = message.payload.decode('utf-8')
    data = json.loads(payload)

    print(data)

def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        # Perform necessary operations with the received data
        print(f"Received `{msg.payload.decode()}` from `{msg.topic}` topic")

    client.subscribe(TOPIC, qos=0)
    client.on_message = on_message

async def mqtt_subscribe():
    # Set up the MQTT client
    print("function subscribe")
    client = connect_mqtt()
    subscribe(client)
    print("OK")


if __name__ == "__main__":
    asyncio.run(mqtt_subscribe())
