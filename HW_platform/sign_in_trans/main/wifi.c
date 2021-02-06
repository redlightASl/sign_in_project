#include "app_wifi.h"

static const char *TAG = "app_wifi"

/* init wifi ap and station */
void WIFI_init(void)
{
    wifi_init_config_t wifi_cfg = WIFI_INIT_CONFIG_DEFAULT();//set default wifi value

    wifi_config_t sta_config = {
        .sta = {
            .ssid = TARGET_ESP_WIFI_SSID,
            .password = TARGET_ESP_WIFI_PASS,
            .bssid_set = 0
        }
    };
    wifi_config_t ap_config = {
        .ap = {
            .ssid = AP_ESP_WIFI_SSID,
            .password = AP_ESP_WIFI_PASS,
            .ssid_len = 0,
            .max_connection = AP_MAX_STA_CONN,
            .authmode = WIFI_AUTH_WPA_PSK
        }
    };

    //dafault settings
    ESP_ERROR_CHECK(esp_wifi_init(&wifi_cfg));
    //config wifi ap-sta mode
    ESP_ERROR_CHECK(esp_wifi_set_storage(WIFI_STORAGE_RAM));
    ESP_ERROR_CHECK(esp_wifi_set_mode(WIFI_MODE_APSTA));
    //apply settings
    ESP_ERROR_CHECK(esp_wifi_set_config(WIFI_IF_STA, &sta_config));
    ESP_ERROR_CHECK(esp_wifi_set_config(WIFI_IF_AP, &ap_config));
    //start application
    ESP_ERROR_CHECK(esp_wifi_start());
    ESP_LOGI(TAG, "WiFi_init_AP_STA success\n");
    esp_wifi_connect();
}


