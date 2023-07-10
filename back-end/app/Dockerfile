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

# Expose the required port
EXPOSE 8000

# Set the entrypoint command
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
