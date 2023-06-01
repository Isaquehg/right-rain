import asyncio
import json
import logging
import os
import ssl
import paho.mqtt.client as mqtt

# Load the certificate and keys from disk
def load_certificates():
    # Build the file paths
    cert_path = os.path.join(os.path.dirname(__file__), 'certs', 'device_cert.pem.crt')
    key_path = os.path.join(os.path.dirname(__file__), 'certs', 'private.pem.key')
    print(cert_path)
    print(key_path)

    # Load the certificate and private key from the files
    with open("/home/isaquehg/certs/device_cert.pem.crt", 'r') as f:
        certificate = f.read()
    with open("/home/isaquehg/Desktop/right-rain/back-end/app/certs/private.pem.key", 'r') as f:
        private_key = f.read()

    return certificate, private_key

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        logging.info("Connected to AWS IoT")
    else:
        logging.error("Failed to connect to AWS IoT. Error code: %s", rc)

def on_message(client, userdata, message, db):
    payload = message.payload.decode('utf-8')
    data = json.loads(payload)

    db["devices"].insert_one(data)
    print(data)

async def mqtt_subscribe(db):
    # Load the certificates
    #certificate, private_key = load_certificates()

    # Set up the MQTT client
    client = mqtt.Client(client_id='client_id')
    client.tls_set(certfile="/home/isaquehg/certs/device_cert.pem.crt", keyfile="/home/isaquehg/certs/private.pem.key")
    client.on_connect = on_connect
    client.on_message = on_message(db)

    # Connect to the MQTT broker through AWS IoT Core
    endpoint = "ar6fnfi93vtrn-ats.iot.us-east-2.amazonaws.com"
    client.connect(endpoint, port=8883)

    # Subscribe to a topic
    client.subscribe('rightrain/data', qos=1)

    # Keep the client loop running to receive messages
    try:
        client.loop_forever()
    except KeyboardInterrupt:
        client.disconnect()
