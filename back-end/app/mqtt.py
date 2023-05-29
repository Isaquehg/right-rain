import asyncio
import json
from aiomqtt import Client, Message
import ssl

# Load the certificate and keys from disk
def load_certificates():
    with open('back-end/app/certs/device_cert.pem.crt', 'r') as f:
        certificate = f.read()
    with open('private.pem.key', 'r') as f:
        private_key = f.read()
    
    return certificate, private_key

async def on_message_received(message: Message, db):
    payload = message.payload.decode('utf-8')
    data = json.loads(payload)

    # Inserir os dados no banco de dados
    await db["devices"].insert_one(data)

async def mqtt_subscribe(db):
    # Load the certificates
    certificate, private_key = load_certificates()

    # Set up the MQTT client
    client = Client('client_id', ssl=ssl.create_default_context(purpose=ssl.Purpose.CLIENT_AUTH))
    client.tls_set(certfile=certificate, keyfile=private_key)

    # Connect to the MQTT broker through AWS IoT Core
    endpoint = "ar6fnfi93vtrn-ats.iot.us-east-2.amazonaws.com"
    await client.connect(endpoint, port=8883)

    # Subscribe to a topic
    await client.subscribe('rightrain/data', qos=1)

    # Set up the message received callback
    client.on_message = lambda client, message: on_message_received(message, db)

    # Keep the event loop running to receive messages
    try:
        while True:
            await asyncio.sleep(1)
    finally:
        await client.disconnect()
