# Use a base image
FROM python:3.10.6

# Set the working directory
WORKDIR /app

# Copy the application files
COPY app.py /app/app.py
COPY auth.py /app/auth.py

# Install Python dependencies
COPY requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r /app/requirements.txt

# Set the entrypoint command
CMD ["app.handler"]
