/* WiFi and HTTP Server Settings */
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

const char* WIFI_TAG="MyWiFi";
const int wifi_retry_num = 0;

/* WiFi Settings */
#define TARGET_WIFI_SSID "testpoint"
#define TARGET_WIFI_PASS "zvnxf4rb"
#define TARGET_WIFI_MAX_RETRY 5

EventGroupHandle_t s_wifi_event_group;
void wifi_sta_event_handler(void* arg, esp_event_base_t event_base,int32_t event_id, void* event_data);
void ESP_wifi_init(void);
#endif