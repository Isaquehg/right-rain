import asyncio
import json
import logging
import os
import motor
import paho.mqtt.client as mqtt

TOPIC = "rightrain/data"
PORT = 8883
ENDPOINT = "ar6fnfi93vtrn-ats.iot.us-east-2.amazonaws.com"
DEVICE_CERT = os.environ["DEVICE_CERT"]
PRIVATE_KEY = os.environ["PRIVATE_KEY"]
client = motor.motor_asyncio.AsyncIOMotorClient(os.environ["MONGODB_URL"])
db = client.rightrain

# Function to deal with MQTT errors
def on_connect(client, userdata, flags, rc):
    print("on_connect function")
    if rc == 0:
        logging.info("Connected to AWS IoT")
    else:
        logging.error("Failed to connect to AWS IoT. Error code: %s", rc)

def on_message(client, userdata, message):
    print("on_message function")
    payload = message.payload.decode('utf-8')
    data = json.loads(payload)

    db["devices"].insert_one(data)
    print(data)

async def mqtt_subscribe():
    # Set up the MQTT client
    print("function subscribe")
    client = mqtt.Client(client_id='client_id')
    client.tls_set(certfile=DEVICE_CERT, keyfile=PRIVATE_KEY)
    client.on_connect = on_connect
    client.on_message = on_message

    # Connect to the MQTT broker through AWS IoT Core
    client.connect(ENDPOINT, PORT)
    print("connect")

    # Subscribe to a topic
    client.subscribe(TOPIC, qos=1)

    # Keep the client loop running to receive messages
    try:
        print("looping...")
        client.loop_forever()
    except KeyboardInterrupt:
        print("disconnected")
        client.disconnect()

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)  # Enable logging
    asyncio.run(mqtt_subscribe())