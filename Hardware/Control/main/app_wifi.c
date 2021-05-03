#include <string.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "sys/param.h"

#include "esp_wifi.h"
#include "esp_netif.h"
#include "esp_eth.h"
#include "lwip/err.h"
#include "lwip/sys.h"
#include "lwip/sockets.h"
#include "lwip/netdb.h"
#include "lwip/dns.h"

#include "esp_tls.h"
#include "esp_crt_bundle.h"

#include "app_wifi.h"

static const char *WIFI_TAG="MyWiFi";
static const int wifi_retry_num=0;

static const char *REQUEST = "GET " WEB_URL " HTTP/1.0\r\n"
                             "Host: "WEB_SERVER"\r\n"
                             "User-Agent: esp-idf/1.0 esp32\r\n"
                             "\r\n";

/* Connection */
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

    ESP_ERROR_CHECK(esp_wifi_set_config(ESP_IF_WIFI_STA, &wifi_config));

    ESP_LOGI(WIFI_TAG, "wifi_init_sta finished.");
    ESP_LOGI(WIFI_TAG, "connect to ap SSID:%s password:%s",
             TARGET_WIFI_SSID, TARGET_WIFI_PASS);
}

void ESP_wifi_init(void)
{
    s_wifi_event_group = xEventGroupCreate();

    wifi_init_config_t cfg = WIFI_INIT_CONFIG_DEFAULT();

    // pre set wifi config
    tcpip_adapter_init();
    ESP_ERROR_CHECK(esp_netif_init());
    ESP_ERROR_CHECK(esp_event_loop_init(event_handler, NULL));

    ESP_ERROR_CHECK(esp_wifi_init(&cfg));
    ESP_ERROR_CHECK(esp_wifi_set_mode(WIFI_MODE_STA));

    wifi_init_sta();// set wifi config

    ESP_ERROR_CHECK(esp_wifi_start());// boot wifi
    ESP_ERROR_CHECK(esp_wifi_set_ps(WIFI_PS_NONE));

    ESP_ERROR_CHECK(esp_event_handler_register(WIFI_EVENT, ESP_EVENT_ANY_ID, &event_handler, NULL));
    ESP_ERROR_CHECK(esp_event_handler_register(IP_EVENT, IP_EVENT_STA_GOT_IP, &event_handler, NULL));
}

void TaskUpload(void* param);
{
    char buf[512];
    int ret, len;

    while(1)
    {
        esp_tls_cfg_t cfg = {
            .crt_bundle_attach = esp_crt_bundle_attach,
        };

        struct esp_tls *tls = esp_tls_conn_http_new(WEB_URL, &cfg);

        if(tls != NULL)
        {
            ESP_LOGI(TAG, "Connection established...");
        }
        else
        {
            ESP_LOGE(TAG, "Connection failed...");
            goto exit;
        }

        size_t written_bytes = 0;
        do
        {
            ret = esp_tls_conn_write(tls,
                                     REQUEST + written_bytes,
                                     strlen(REQUEST) - written_bytes);
            if (ret >= 0)
            {
                ESP_LOGI(TAG, "%d bytes written", ret);
                written_bytes += ret;
            }
            else if (ret != ESP_TLS_ERR_SSL_WANT_READ  && ret != ESP_TLS_ERR_SSL_WANT_WRITE)
            {
                ESP_LOGE(TAG, "esp_tls_conn_write  returned 0x%x", ret);
                goto exit;
            }
        } while(written_bytes < strlen(REQUEST));

        ESP_LOGI(TAG, "Reading HTTP response...");
        do
        {
            len = sizeof(buf) - 1;
            bzero(buf, sizeof(buf));
            ret = esp_tls_conn_read(tls, (char *)buf, len);

            if(ret == ESP_TLS_ERR_SSL_WANT_WRITE  || ret == ESP_TLS_ERR_SSL_WANT_READ)
                continue;

            if(ret < 0)
            {
                ESP_LOGE(TAG, "esp_tls_conn_read  returned -0x%x", -ret);
                break;
            }

            if(ret == 0)
            {
                ESP_LOGI(TAG, "connection closed");
                break;
            }

            len = ret;
            ESP_LOGD(TAG, "%d bytes read", len);
            /* Print response directly to stdout as it is read */
            for(int i = 0; i < len; i++)
            {
                putchar(buf[i]);
            }
        } while(1);

exit:
        esp_tls_conn_delete(tls);
        putchar('\n'); // JSON output doesn't have a newline at end

        static int request_count;
        ESP_LOGI(TAG, "Completed %d requests", ++request_count);

        for(int countdown = 10; countdown >= 0; countdown--) {
            ESP_LOGI(TAG, "%d...", countdown);
            vTaskDelay(1000 / portTICK_PERIOD_MS);
        }
        ESP_LOGI(TAG, "Starting again!");
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