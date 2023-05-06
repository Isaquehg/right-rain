import boto3
import os

def connect_to_iot():
    # Set up the AWS IoT client
    session = boto3.Session(
        aws_access_key_id=os.environ['AWS_ACCESS_KEY_ID'],
        aws_secret_access_key=os.environ['AWS_SECRET_ACCESS_KEY'],
        region_name='us-east-1'
    )
    iot = session.client('iot')
    
    # Get the endpoint for your AWS IoT Core account
    response = iot.describe_endpoint(endpointType='iot:Data-ATS')
    endpoint = response['endpointAddress']
    
    return endpoint

def load_certificates():
    # Load the certificate files from disk
    with open('certificate.pem.crt', 'r') as f:
        certificate = f.read()
    with open('private.pem.key', 'r') as f:
        private_key = f.read()
    with open('root-ca.pem', 'r') as f:
        root_ca = f.read()
    
    return certificate, private_key, root_ca
