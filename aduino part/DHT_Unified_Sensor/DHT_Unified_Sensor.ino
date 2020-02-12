//#include <BH1750FVI.h>
#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>
#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <Wire.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

//temperature and humidity pin
#define DHTPIN 2 //D4
#define DHTTYPE DHT11

DHT_Unified dht(DHTPIN, DHTTYPE);

//light
//unsigned int AnalogLight;
//BH1750FVI LightSensor(BH1750FVI::k_DevModeContLowRes);

//firebase
#define FIREBASE_HOST "network-management-syste-a1108.firebaseio.com"
#define FIREBASE_AUTH "g3Xw6DoVwZcXixbW4laP2hukjx3ykw8C612lvXMx"
#define WIFI_SSID "DESKTOP-4FCKRMC 8934"
#define WIFI_PASSWORD "2T)926f9"

// time
String formattedDate;

const long utcOffsetInSeconds = 19800;

WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "asia.pool.ntp.org", utcOffsetInSeconds);

FirebaseData firebaseData;
FirebaseJson json;
void printResult(FirebaseData &data);

uint32_t delayMS;

void setup() {
  pinMode (5,OUTPUT);
  Serial.begin(9600);

  //teperature and humidity sensor
  dht.begin();
  sensor_t sensor;
  dht.temperature().getSensor(&sensor);
  dht.humidity().getSensor(&sensor);

  //delayMS = sensor.min_delay/1000000*60000;
  delayMS = sensor.min_delay/1000;

    //wifi conectivity
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  timeClient.begin();

  //firebase connectivity
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
  Firebase.setReadTimeout(firebaseData, 1000 * 60);

  Firebase.setInt(firebaseData,"LED_STATUS",0);
}

void loop() {
  delay(delayMS);
  
  timeClient.update();
  formattedDate = timeClient.getFormattedTime();

  //AnalogLight = analogRead(A0);
  
  //Serial.print(F("Light: "));
  //Serial.print(AnalogLight);
  //Serial.println(F("not converted"));
  //Firebase.setDouble(firebaseData, "/Light/" +, AnalogLight);

  sensors_event_t event;
  dht.temperature().getEvent(&event);

  if (isnan(event.temperature)) {
    Serial.println(F("Error reading temperature!"));
  }
  else {
    Serial.print(F("Temperature: "));
    Serial.print(event.temperature);
    Serial.println(F("°C"));
    Firebase.setDouble(firebaseData, "/Temperature", event.temperature);
    //Firebase.set(firebaseData, "/Temperature/Time", formattedDate);
  }

  dht.humidity().getEvent(&event);

  if (isnan(event.relative_humidity)) {
    Serial.println(F("Error reading humidity!"));
  }
  else {
    Serial.print(F("Humidity: "));
    Serial.print(event.relative_humidity);
    Serial.println(F("%"));
    Firebase.setDouble(firebaseData, "/Humidity", event.relative_humidity);
    //Firebase.set(firebaseData, "/Humidity/Time", formattedDate);
  }
  LED();
}
int n;
void LED(){
  
  Firebase.getInt(firebaseData,"LED_STATUS",n);
  if(n==0){
    digitalWrite(5,LOW);
    Serial.println(n);
    }
  else if(n==1){
    digitalWrite(5,HIGH);
    Serial.println(n);
    }
  else{
    digitalWrite(5,LOW);
    Serial.println("error lightning");
    }
  }
