#include <string.h>
#include "sys/param.h"

#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "esp_wifi_default.h"
#include "esp_wifi.h"
#include "esp_eth.h"
#include "esp_netif.h"

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"

#include "lwip/err.h"
#include "lwip/sys.h"
#include "lwip/sockets.h"
#include "lwip/netdb.h"
#include "lwip/dns.h"

#include "esp_websocket_client.h"
#include "addr_from_stdin.h"

#include "app_wifi.h"
#include "app_main.h"

static const char *WIFI_TAG="MyWiFi";
static const char *HTTP_TAG="MyWebSocket";

static const int wifi_retry_num=0;
static const char *GET_TIME_REQUEST = "GET " WEB_PATH_GET_TIME " HTTP/1.0\r\n"
                                      "Host: " WEB_SERVER ":" WEB_PORT "\r\n"
                                      "\r\n";

static char *SIGNIN_REQUEST = "POST " WEB_PATH_POST_SIGNIN " HTTP/1.0\r\n"
                              "Host: " WEB_SERVER ":" WEB_PORT "\r\n"
                              "fingerprint: 8399d88e3293cc89cacc1d735af12810,"//undefined faceid fingerprint
                              "location: B102,"//undefined location codes
                              "timestamp: 1618405164222"//undefined timestamp
                              "\r\n";

/* WiFi Connection FSM*/
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

void wifi_init_sta(void)
{
    wifi_config_t wifi_config;
    memset(&wifi_config, 0, sizeof(wifi_config_t));

    snprintf((char*)wifi_config.sta.ssid, 32, "%s", TARGET_WIFI_SSID);
    snprintf((char*)wifi_config.sta.password, 64, "%s", TARGET_WIFI_PASS);

    ESP_ERROR_CHECK(esp_wifi_set_config(ESP_IF_WIFI_STA, &wifi_config));

    ESP_LOGI(WIFI_TAG, "wifi_init_sta finished");
    ESP_LOGI(WIFI_TAG, "connect to ap SSID:%s password:%s",
             TARGET_WIFI_SSID, TARGET_WIFI_PASS);
}

void ESP_wifi_init(void)
{
    s_wifi_event_group = xEventGroupCreate();

    wifi_init_config_t cfg = WIFI_INIT_CONFIG_DEFAULT();

    // preset wifi config
    // tcpip_adapter_init();
    ESP_ERROR_CHECK(esp_netif_init());
    ESP_ERROR_CHECK(esp_event_loop_init(event_handler, NULL));

    ESP_ERROR_CHECK(esp_wifi_init(&cfg));
    ESP_ERROR_CHECK(esp_wifi_set_mode(WIFI_MODE_STA));

    wifi_init_sta(); //set wifi config

    ESP_ERROR_CHECK(esp_wifi_start()); //boot wifi
    ESP_ERROR_CHECK(esp_wifi_set_ps(WIFI_PS_NONE));

    ESP_ERROR_CHECK(esp_event_handler_register(WIFI_EVENT, ESP_EVENT_ANY_ID, &event_handler, NULL));
    ESP_ERROR_CHECK(esp_event_handler_register(IP_EVENT, IP_EVENT_STA_GOT_IP, &event_handler, NULL));

    Create_HTTP_Connection();
}

void Create_HTTP_Connection(void)
{
    int ret = 0;
    const struct addrinfo hints = {
        .ai_family = AF_INET,
        .ai_socktype = SOCK_STREAM,
        .ai_addr = NULL
    };
    struct addrinfo *res;
    struct in_addr *addr;
    int sock = 0;
    int r = 0;
    char rx_buffer[SOCKET_RECEIVE_BUFFER_SIZE];

    do /* get address infomation */
    {
        ret = getaddrinfo(WEB_SERVER, WEB_PORT, &hints, &res);
        ESP_LOGE(HTTP_TAG, "DNS lookup failed ret=%d res=%p...", ret, res);
        vTaskDelay(1000 / portTICK_PERIOD_MS);
    } while (ret == 0 && res != NULL);
    
    /* create HTTP connection */
    addr = &((struct sockaddr_in *)res->ai_addr)->sin_addr;
    ESP_LOGI(HTTP_TAG, "DNS lookup succeeded. IP=%s...", inet_ntoa(*addr));

    while(1)
    {
        sock = socket(res->ai_family, res->ai_socktype, 0);
        if(s < 0)
        {
            ESP_LOGE(HTTP_TAG, "Failed to allocate socket...");
            freeaddrinfo(res);
            vTaskDelay(1000 / portTICK_PERIOD_MS);
            continue;
        }
    }
    ESP_LOGI(HTTP_TAG, "allocated socket...");
    
    while(1)
    {
        if(connect(sock, res->ai_addr, res->ai_addrlen) != 0)
        {
            ESP_LOGE(HTTP_TAG, "socket connect failed errno=%d...", errno);
            close(sock);
            freeaddrinfo(res);
            vTaskDelay(4000 / portTICK_PERIOD_MS);
            continue;
        }
    }
    ESP_LOGI(HTTP_TAG, "connected!");
    freeaddrinfo(res);
}

/* 
 * if received SPI messages,package them up and upload the messages 
 * else keep silent 
 */
void TaskUpload(void* param);
{
    /* undefined switch conditions */


    while(1)
    {
        /* send HTTP message */
        if (write(sock, REQUEST, strlen(REQUEST)) < 0)
        {
            ESP_LOGE(TAG, "... socket send failed");
            close(sock);
            vTaskDelay(4000 / portTICK_PERIOD_MS);
            continue;
        }
        ESP_LOGI(TAG, "... socket send success");

        /* receive HTTP response */
        struct timeval receiving_timeout;
        receiving_timeout.tv_sec = 5;
        receiving_timeout.tv_usec = 0;
        if (setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &receiving_timeout, sizeof(receiving_timeout)) < 0)
        {
            ESP_LOGE(TAG, "... failed to set socket receiving timeout");
            close(sock);
            vTaskDelay(4000 / portTICK_PERIOD_MS);
            continue;
        }
        ESP_LOGI(TAG, "... set socket receiving timeout success");

        /* Read HTTP response */
        do
        {
            bzero(recv_buf, sizeof(recv_buf));
            ret = read(sock, recv_buf, sizeof(recv_buf)-1);
            for(int i = 0; i < ret; i++)
            {
                putchar(recv_buf[i]);
            }
        } while(ret > 0);
    }
    vTaskDelete();
}

void TaskListen(void* param)
{
    /* undefined switch conditions */

    
    while(1)
    {
        /* send HTTP message */
        if (write(sock, REQUEST, strlen(REQUEST)) < 0)
        {
            ESP_LOGE(TAG, "... socket send failed");
            close(sock);
            vTaskDelay(4000 / portTICK_PERIOD_MS);
            continue;
        }
        ESP_LOGI(TAG, "... socket send success");

        /* receive HTTP response */
        struct timeval receiving_timeout;
        receiving_timeout.tv_sec = 5;
        receiving_timeout.tv_usec = 0;
        if (setsockopt(sock, SOL_SOCKET, SO_RCVTIMEO, &receiving_timeout, sizeof(receiving_timeout)) < 0)
        {
            ESP_LOGE(TAG, "... failed to set socket receiving timeout");
            close(sock);
            vTaskDelay(4000 / portTICK_PERIOD_MS);
            continue;
        }
        ESP_LOGI(TAG, "... set socket receiving timeout success");

        /* Read HTTP response */
        do
        {
            bzero(recv_buf, sizeof(recv_buf));
            ret = read(sock, recv_buf, sizeof(recv_buf)-1);
            for(int i = 0; i < ret; i++)
            {
                putchar(recv_buf[i]);
            }
        } while(ret > 0);
    }
    vTaskDelete();
}