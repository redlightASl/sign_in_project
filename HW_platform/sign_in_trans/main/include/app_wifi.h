#ifndef __APP_WIFI_H
#define __APP_WIFI_H

#include "esp_log.h"
#include "nvs.h"
#include "nvs_flash.h"
#include "esp_wifi.h"

/* WiFi Settings */
#define TARGET_ESP_WIFI_SSID "CU_P49d"
#define TARGET_ESP_WIFI_PASS "zvnxf4rb"
#define ESP_MAXIMUM_RETRY 5

#define AP_ESP_WIFI_SSID "MyWiFi"
#define AP_ESP_WIFI_PASS "hellomywifi"
#define AP_ESP_WIFI_CHANNEL 1
#define AP_MAX_STA_CONN 2

void WIFI_init(void);

#endif