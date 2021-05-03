/*
* WiFi and HTTP Server Settings  
* 
*/
#ifndef __WIFI_H
#define __WIFI_H

/* WiFi Settings */
#define TARGET_WIFI_SSID "testpoint"
#define TARGET_WIFI_PASS "abcd1234"
#define TARGET_WIFI_MAX_RETRY 5
#define SET_MAX_STA_CONN        CONFIG_MAX_STA_CONN
#define SET_IP_ADDR             CONFIG_SERVER_IP
#define SET_ESP_WIFI_AP_CHANNEL CONFIG_ESP_WIFI_AP_CHANNEL

#define WEB_SERVER "http://api.dawncraft.cc/"
#define WEB_PORT "443"
#define WEB_URL "http://api.dawncraft.cc/???"

void ESP_wifi_init(void);
void TaskUpload(void* param);
void TaskListen(void* param);

#endif