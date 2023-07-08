# Use an official Python runtime as the base image
FROM python:3.10.6

# Set the working directory in the container
WORKDIR /app

# Copy the requirements file
COPY requirements.txt .

# MQTT TLS cert
COPY certs/emqxsl-ca.crt /app/certs/emqxsl-ca.crt

# Install the Python dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the rest of the application code
COPY . .

# Expose the necessary port
EXPOSE 80

# Set the entrypoint command
CMD ["python", "mqtt_process.py"]
