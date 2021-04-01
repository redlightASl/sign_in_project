/*
* WiFi and HTTP Server Settings  
* 
*/
#ifndef __WIFI_H
#define __WIFI_H

#include <string.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "sys/param.h"

#include "esp_wifi.h"
#include "esp_eth.h"
#include "lwip/err.h"
#include "lwip/sys.h"

/* WiFi Settings */
#define TARGET_WIFI_SSID "testpoint"
#define TARGET_WIFI_PASS "abcd1234"
#define TARGET_WIFI_MAX_RETRY 5
#define SET_MAX_STA_CONN        CONFIG_MAX_STA_CONN
#define SET_IP_ADDR             CONFIG_SERVER_IP
#define SET_ESP_WIFI_AP_CHANNEL CONFIG_ESP_WIFI_AP_CHANNEL

void ESP_wifi_init(void);
void TaskUpload(void* param);
void TaskListen(void* param);

#endif