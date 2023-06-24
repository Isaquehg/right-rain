#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <WiFiClientSecureBearSSL.h>
#include "DHT.h"

// WiFi
const char* ssid = "Ap 106"; // Insira o nome da sua rede WiFi
const char* password = "inatel123";  // Insira a senha da sua rede WiFi

// MQTT Broker
const char* mqtt_broker = "fbe1817f.ala.us-east-1.emqxsl.com"; // Endereço do broker
const int mqtt_port = 8883; // Porta MQTT sobre TLS
const char* topic = "esp8266/test"; // Tópico MQTT
const char* mqtt_username = "isaquehg"; // Nome de usuário para autenticação MQTT
const char* mqtt_password = "1arry_3arry"; // Senha para autenticação MQTT

// DHT11
#define DHTPIN 4 // Pino de dados do sensor DHT11
#define DHTTYPE DHT11 // Tipo do sensor DHT11
DHT dht(DHTPIN, DHTTYPE);

// WiFi Client
WiFiClientSecure espClient;
PubSubClient client(espClient);

const char* fingerprint = "42:AE:D8:A3:42:F1:C4:1F:CD:64:9C:D7:4B:A1:EE:5B:5E:D7:E2:B5";

void setup() {
  Serial.begin(115200);
  
  // Conexão com a rede WiFi
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to the WiFi network");

  // Configuração do sensor DHT11
  dht.begin();

  // Configuração do cliente MQTT
  espClient.setFingerprint(fingerprint);
  client.setServer(mqtt_broker, mqtt_port);
  client.setCallback(callback);

  // Conexão ao broker MQTT
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

  // Publicação e subscrição
  client.publish(topic, "hello emqx"); // Publica uma mensagem no tópico
  client.subscribe(topic); // Subscreve ao tópico
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

  // Leitura do sensor DHT11
  float temperature = dht.readTemperature();
  float humidity = dht.readHumidity();

  // Publicação dos valores no tópico
  String message = "Temperature: " + String(temperature) + "°C, Humidity: " + String(humidity) + "%";
  client.publish(topic, message.c_str());
  
  delay(5000); // Aguarda 5 segundos antes de fazer uma nova leitura e publicação
}
