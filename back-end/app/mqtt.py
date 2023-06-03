import asyncio
import json
import logging
import os
from AWSIoTPythonSDK.MQTTLib import AWSIoTMQTTClient

TOPIC = "rightrain/data"
PORT = 8883
ENDPOINT = "ar6fnfi93vtrn-ats.iot.us-east-2.amazonaws.com"
THING_NAME = "fast_api"
DEVICE_CERT = os.environ["DEVICE_CERT"]
PRIVATE_KEY = os.environ["PRIVATE_KEY"]
ROOT_CA = os.environ["ROOT_CA"]

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

    # Perform necessary operations with the received data
    print(data)

async def mqtt_subscribe():
    # Set up the MQTT client
    print("function subscribe")
    mqtt_client = AWSIoTMQTTClient(THING_NAME)
    mqtt_client.configureEndpoint(ENDPOINT, PORT)
    mqtt_client.configureCredentials(ROOT_CA, PRIVATE_KEY, DEVICE_CERT)

    # Set the connect and message callback functions
    mqtt_client.onConnect = on_connect
    mqtt_client.onMessage = on_message

    # Connect to AWS IoT Core
    mqtt_client.connect()

    # Subscribe to the topic
    mqtt_client.subscribe(TOPIC, 1)

    print("OK")

    # Keep the client loop running to receive messages
    try:
        print("looping...")
        mqtt_client.loop_forever()
    except KeyboardInterrupt:
        print("disconnected")
        mqtt_client.disconnect()

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO)  # Enable logging
    asyncio.run(mqtt_subscribe())
