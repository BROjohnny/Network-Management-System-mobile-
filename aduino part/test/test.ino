#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <DHT_U.h>

dht DHT;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  int val = DHT.read11(D4);
  Serial.println(DHT.temperature);
  delay(1000);
}
