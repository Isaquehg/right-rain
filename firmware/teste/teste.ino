#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <WiFiClientSecureBearSSL.h>
#include "DHT.h"
#include <NTPClient.h>
#include <WiFiUdp.h>

// WiFi
const char* ssid = "WLL-Inatel";
const char* password = "inatelsemfio";

// MQTT Broker
const char* mqtt_broker = "fbe1817f.ala.us-east-1.emqxsl.com";
const int mqtt_port = 8883;
const char* topic = "rightrain/data";
const char* mqtt_username = "isaquehg";
const char* mqtt_password = "1arry_3arry";

// DHT11
#define DHTPIN 2
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

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
    message += "\"d_id\": \"abcdefghijk\",";
    message += "\"d_name\": \"Fetin Device\",";
    message += "\"latitude\": " + String(-22.2583, 6) + ",";
    message += "\"longitude\": " + String(-45.6963, 6) + ",";
    message += "\"date\": \"" + currentDateTime + "\",";
    message += "\"temperature\": " + String(temperature, 1) + ",";
    message += "\"air_humidity\": " + String(humidity);
    message += "}";

    client.publish(topic, message.c_str());
    Serial.println(message);

    delay(5000);
}

String getFormattedDateTime(unsigned long epochTime) {
    unsigned long seconds = epochTime;
    unsigned long minutes = seconds / 60;
    unsigned long hours = minutes / 60;
    unsigned long days = hours / 24;
    unsigned long years = 1970;

    // Calculate years, considering leap years
    while (days >= 365) {
        if (years % 4 == 0 && (years % 100 != 0 || years % 400 == 0)) {
            if (days >= 366) {
                days -= 366;
                years++;
            } else {
                break;
            }
        } else {
            days -= 365;
            years++;
        }
    }

    // Calculate months and days
    unsigned long monthDays[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    if (years % 4 == 0 && (years % 100 != 0 || years % 400 == 0)) {
        monthDays[1] = 29; // Leap year
    }
    unsigned long month = 1;
    while (days >= monthDays[month - 1]) {
        days -= monthDays[month - 1];
        month++;
    }

    // Calculate hours, minutes, and seconds
    hours %= 24;
    minutes %= 60;
    seconds %= 60;

    char buffer[20];
    snprintf(buffer, sizeof(buffer), "%04lu-%02lu-%02luT%02lu:%02lu:%02lu", years, month, days + 1, hours, minutes, seconds);

    return String(buffer);
}
