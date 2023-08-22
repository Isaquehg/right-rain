#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <WiFiClientSecureBearSSL.h>
#include "DHT.h"
#include <TinyGPS++.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

// WiFi
const char* ssid = "Ap 106";
const char* password = "inatel123";

// MQTT Broker
const char* mqtt_broker = "fbe1817f.ala.us-east-1.emqxsl.com";
const int mqtt_port = 8883;
const char* topic = "rightrain/data";
const char* mqtt_username = "isaquehg";
const char* mqtt_password = "1arry_3arry";

// DHT11
#define DHTPIN 4
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// GPS
TinyGPSPlus gps;

// WiFi Client
WiFiClientSecure espClient;
PubSubClient client(espClient);

const char* fingerprint = "42:AE:D8:A3:42:F1:C4:1F:CD:64:9C:D7:4B:A1:EE:5B:5E:D7:E2:B5";

// NTP
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org");

void setup() {
    Serial.begin(115200);

    Serial1.begin(9600);
    delay(1000);

    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi...");
    }
    Serial.println("Connected to the WiFi network");

    dht.begin();

    espClient.setFingerprint(fingerprint);
    client.setServer(mqtt_broker, mqtt_port);
    client.setCallback(callback);

    timeClient.begin();
    timeClient.setTimeOffset(0);

    while (!client.connected()) {
        String client_id = "esp8266-client-";
        client_id += String(WiFi.macAddress());
        Serial.printf("The client %s connects to the MQTT broker\n", client_id.c_str());
        if (client.connect(client_id.c_str(), mqtt_username, mqtt_password)) {
            Serial.println("Connected to MQTT broker.");
        } else {
            Serial.print("Failed to connect to MQTT broker, rc=");
            Serial.print(client.state());
            Serial.println(" Retrying in 5 seconds.");
            delay(5000);
        }
    }
}

void callback(char* topic, byte* payload, unsigned int length) {
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message: ");
    for (int i = 0; i < length; i++) {
        Serial.print((char)payload[i]);
    }
    Serial.println();
    Serial.println("-----------------------");
}

void loop() {
    client.loop();
    timeClient.update();

    String currentDateTime = getFormattedDateTime(timeClient.getEpochTime());

    float temperature = dht.readTemperature();
    float humidity = dht.readHumidity();

    if (!isnan(temperature) && !isnan(humidity)) {
        // ... (your sensor reading code)
    } else {
        Serial.println("Failed to read from DHT sensor!");
    }

    String message = "{";
    message += "\"u_id\": \"64caccb46b1a8787775d075d\",";
    message += "\"d_id\": \"plmokmuhbtrver\",";
    message += "\"d_name\": \"Fetin Device\",";
    message += "\"latitude\": " + String(-21.6804, 6) + ",";
    message += "\"longitude\": " + String(-45.9190, 6) + ",";
    message += "\"date\": \"" + currentDateTime + "\",";
    message += "\"temperature\": " + String(temperature, 1) + ",";
    message += "\"air_humidity\": " + String(humidity);
    message += "}";

    client.publish(topic, message.c_str());
    Serial.println(message);

    delay(5000);
}

String getFormattedDateTime(unsigned long epochTime) {
    unsigned long year, month, day, hour, minute, second;

    // Extract date and time components from epochTime
    second = epochTime % 60;
    minute = (epochTime / 60) % 60;
    hour = (epochTime / 3600) % 24;
    day = (epochTime / 86400) % 31 + 1; // Assuming each month has 31 days
    month = (epochTime / 2592000) % 12 + 1; // Assuming there are 30.44 days in a month (on average)
    year = (epochTime / 31536000UL) + 1970; // 31536000 seconds in a year

    char buffer[20];
    snprintf(buffer, sizeof(buffer), "%04lu-%02lu-%02luT%02lu:%02lu:%02lu", year, month, day, hour, minute, second);

    return String(buffer);
}
