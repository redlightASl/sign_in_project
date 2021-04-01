#include "app_wifi.h"

static const char *WIFI_TAG="MyWiFi";
static const int wifi_retry_num=0;

static esp_err_t event_handler(void *ctx, system_event_t *event)
{
    switch(event->event_id)
    {
    case SYSTEM_EVENT_AP_STACONNECTED:
        {
            ESP_LOGI(WIFI_TAG, "station:" MACSTR " join, AID=%d",
                 MAC2STR(event->event_info.sta_connected.mac),
                 event->event_info.sta_connected.aid);
            break;
        }
    case SYSTEM_EVENT_AP_STADISCONNECTED:
        {
            ESP_LOGI(WIFI_TAG, "station:" MACSTR "leave, AID=%d",
                 MAC2STR(event->event_info.sta_disconnected.mac),
                 event->event_info.sta_disconnected.aid);
            break;
        }
    case SYSTEM_EVENT_STA_START:
        {
            esp_wifi_connect();
            break;
        }
    case SYSTEM_EVENT_STA_GOT_IP:
        {
            ESP_LOGI(WIFI_TAG, "got ip:%s",ip4addr_ntoa(&event->event_info.got_ip.ip_info.ip));
            wifi_retry_num = 0;
            break;
        }
    case SYSTEM_EVENT_STA_DISCONNECTED:
        {
            if (wifi_retry_num < TARGET_WIFI_MAX_RETRY)
            {
                esp_wifi_connect();
                wifi_retry_num++;
                ESP_LOGI(WIFI_TAG,"retry to connect to the AP");
            }
            ESP_LOGI(WIFI_TAG,"connect to the AP fail");
            break;
        }
    default:
        break;
    }
    mdns_handle_system_event(ctx, event);
    return ESP_OK;
}

static void wifi_init_sta(void)
{
    wifi_config_t wifi_config;
    memset(&wifi_config, 0, sizeof(wifi_config_t));
    snprintf((char*)wifi_config.sta.ssid, 32, "%s", TARGET_WIFI_SSID);
    snprintf((char*)wifi_config.sta.password, 64, "%s", TARGET_WIFI_PASS);

    ESP_ERROR_CHECK(esp_wifi_set_config(ESP_IF_WIFI_STA, &wifi_config) );

    ESP_LOGI(WIFI_TAG, "wifi_init_sta finished.");
    ESP_LOGI(WIFI_TAG, "connect to ap SSID:%s password:%s",
             TARGET_WIFI_SSID, TARGET_WIFI_PASS);
}

void ESP_wifi_init(void)
{
    wifi_init_config_t cfg = WIFI_INIT_CONFIG_DEFAULT();
    wifi_config_t wifi_config;

    tcpip_adapter_init();
    ESP_ERROR_CHECK(esp_event_loop_init(event_handler, NULL));
    ESP_ERROR_CHECK(esp_wifi_init(&cfg));
    ESP_ERROR_CHECK(esp_wifi_set_mode(mode));
    wifi_init_sta();
    ESP_ERROR_CHECK(esp_wifi_start());
    ESP_ERROR_CHECK(esp_wifi_set_ps(WIFI_PS_NONE));
}

void TaskUpload(void* param);
{
    while(1)
    {


    }
    vTaskDelete();
}

void TaskListen(void* param)
{
    while(1)
    {

    }
    vTaskDelete();
}