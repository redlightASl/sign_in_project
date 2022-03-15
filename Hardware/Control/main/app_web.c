#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/queue.h"
#include "esp_system.h"
#include "esp_wifi.h"
#include "esp_netif.h"
#include "esp_event.h"
#include "esp_log.h"
#include "nvs_flash.h"
#include "esp_http_client.h"
#include "esp_tls.h"

#include "lwip/err.h"
#include "lwip/sockets.h"
#include "lwip/sys.h"
#include "lwip/netdb.h"
#include "lwip/dns.h"
#include "cJSON.h"

#include "app_web.h"
#include "app_main.h"

static const char* TAG = "HTTP_Client";

esp_err_t _http_event_handler(esp_http_client_event_t* evt)
{
    static char* output_buffer; // Buffer to store response of http request from event handler
    static int output_len; // Stores number of bytes read

    switch (evt->event_id)
    {
    case HTTP_EVENT_ERROR:
        ESP_LOGD(TAG, "HTTP_EVENT_ERROR");
        break;
    case HTTP_EVENT_ON_CONNECTED:
        ESP_LOGD(TAG, "HTTP_EVENT_ON_CONNECTED");
        break;
    case HTTP_EVENT_HEADER_SENT:
        ESP_LOGD(TAG, "HTTP_EVENT_HEADER_SENT");
        break;
    case HTTP_EVENT_ON_HEADER:
        ESP_LOGD(TAG, "HTTP_EVENT_ON_HEADER, key=%s, value=%s", evt->header_key, evt->header_value);
        break;
    case HTTP_EVENT_ON_DATA:
        ESP_LOGD(TAG, "HTTP_EVENT_ON_DATA, len=%d", evt->data_len);
        /*
         *  Check for chunked encoding is added as the URL for chunked encoding used in this example returns binary data.
         *  However, event handler can also be used in case chunked encoding is used.
         */
        if (!esp_http_client_is_chunked_response(evt->client))
        {
            // If user_data buffer is configured, copy the response into the buffer
            if (evt->user_data)
            {
                memcpy(evt->user_data + output_len, evt->data, evt->data_len);
            }
            else
            {
                if (output_buffer == NULL)
                {
                    output_buffer = (char*)malloc(esp_http_client_get_content_length(evt->client));
                    output_len = 0;
                    if (output_buffer == NULL)
                    {
                        ESP_LOGE(TAG, "Failed to allocate memory for output buffer");
                        return ESP_FAIL;
                    }
                }
                memcpy(output_buffer + output_len, evt->data, evt->data_len);
            }
            output_len += evt->data_len;
        }
        break;
    case HTTP_EVENT_ON_FINISH:
        ESP_LOGD(TAG, "HTTP_EVENT_ON_FINISH");
        if (output_buffer != NULL)
        {
            // Response is accumulated in output_buffer. Uncomment the below line to print the accumulated response
            // ESP_LOG_BUFFER_HEX(TAG, output_buffer, output_len);
            free(output_buffer);
            output_buffer = NULL;
            output_len = 0;
        }
        break;
    case HTTP_EVENT_DISCONNECTED:
        ESP_LOGI(TAG, "HTTP_EVENT_DISCONNECTED");
        int mbedtls_err = 0;
        esp_err_t err = esp_tls_get_and_clear_last_error(evt->data, &mbedtls_err, NULL);
        if (err != 0)
        {
            if (output_buffer != NULL)
            {
                free(output_buffer);
                output_buffer = NULL;
                output_len = 0;
            }
            ESP_LOGI(TAG, "Last esp error code: 0x%x", err);
            ESP_LOGI(TAG, "Last mbedtls failure: 0x%x", mbedtls_err);
        }
        break;
    }
    return ESP_OK;
}

void Get_timestamp_task(void* pvParameters)
{
    char local_response_buffer[HTTP_GET_BUFFER_SIZE] = { 0 };
    char timestamp[TIMESTAMP_SIZE] = { 0 };

    while (1)
    {
        if (xSemaphoreTake(HTTP_GET_TIMESTAMP_FLAG, portMAX_DELAY) == pdTRUE)
        {
            esp_http_client_config_t config = {
                .host = WEB_SERVER,
                .path = WEB_PATH_GET_TIME,
                .query = WEB_QUERY,
                .transport_type = HTTP_TRANSPORT_OVER_TCP,
                .event_handler = _http_event_handler,
                .user_data = local_response_buffer
            };
            esp_http_client_handle_t client = esp_http_client_init(&config);

            //GET
            esp_err_t err = esp_http_client_perform(client);
            if (err == ESP_OK)
            {
                int content_length = esp_http_client_get_content_length(client);
                ESP_LOGI(TAG, "HTTP GET Status = %d, content_length = %d",
                    esp_http_client_get_status_code(client),
                    content_length);
                ESP_LOGI(TAG, "recv buffer:%s", local_response_buffer);

                cJSON* response_json = cJSON_Parse(local_response_buffer);
                if (response_json != NULL)
                {
                    cJSON* timestamp_json = cJSON_GetObjectItem(response_json, "data");
                    char* timestamp_temp = cJSON_Print(timestamp_json);
                    ESP_LOGI(TAG, "recv number:%s", timestamp_temp);
                    strcpy(timestamp, timestamp_temp);
                    cJSON* message_json = cJSON_GetObjectItem(response_json, "message");
                    char* message = cJSON_Print(message_json);
                    ESP_LOGI(TAG, "recv msg:%s", message);
                    if (!strcmp(message, "null"))
                    {
                        ESP_LOGI(TAG, "senting Queue msg:%s", timestamp);
                        xQueueSend(trans_timestamp_Queue, timestamp, 0);
                    }
                }
                else
                {
                    ESP_LOGE(TAG, "HTTP GET analysis failed");
                }
            }
            else
            {
                ESP_LOGE(TAG, "HTTP GET request failed: %s", esp_err_to_name(err));
            }
            memset(local_response_buffer, 0, HTTP_GET_BUFFER_SIZE * sizeof(char));
            esp_http_client_cleanup(client);
            xSemaphoreGive(HTTP_GET_TIMESTAMP_FLAG);
        }
        taskYIELD();
    }
}

void Post_data_task(void* pvParameters)
{
    char local_response_buffer[HTTP_POST_BUFFER_SIZE] = { 0 };
    char timestamp[TIMESTAMP_SIZE] = { 0 };

    while (1)
    {
        // if (xSemaphoreGive(HTTP_GET_TIMESTAMP_FLAG) == pdTRUE)
        // {
        xQueueReceive(trans_timestamp_Queue, timestamp, portMAX_DELAY);
        ESP_LOGI(TAG, "Got timestamp Queue message: %s", timestamp);
        // xSemaphoreTake(HTTP_GET_TIMESTAMP_FLAG, portMAX_DELAY);
    // }
    // else
    // {
    //     ESP_LOGI(TAG, "Give Sem error");
    // }

    // esp_http_client_config_t config = {
    //     .host = WEB_SERVER,
    //     .path = WEB_PATH_POST_SIGNIN,
    //     .query = WEB_QUERY,
    //     .event_handler = _http_event_handler,
    //     .user_data = local_response_buffer,
    // };
    // esp_http_client_handle_t client = esp_http_client_init(&config);

    // //structure Json Object
    // cJSON* cjson_root = cJSON_CreateObject();
    // cJSON_AddStringToObject(cjson_root, "value", "8399d88e3293cc89cacc1d735af12810");
    // cJSON_AddStringToObject(cjson_root, "location", "classroom");
    // cJSON_AddNumberToObject(cjson_root, "timestamp", timestamp);
    // char* post_data = cJSON_Print(cjson_root);
    // cJSON_Delete(cjson_root);
    // ESP_LOGI(TAG, "generate:%s", post_data);

    // esp_http_client_set_url(client, WEB_SERVER_POST_URL);
    // esp_http_client_set_method(client, HTTP_METHOD_POST);
    // esp_http_client_set_header(client, "Content-Type", "application/json");
    // esp_http_client_set_post_field(client, post_data, strlen(post_data));

    // //POST
    // esp_err_t err = esp_http_client_perform(client);
    // if (err == ESP_OK)
    // {
    //     int content_length = esp_http_client_get_content_length(client);
    //     if (content_length == -1)
    //     {
    //         esp_http_client_get_chunk_length(client, &content_length);
    //     }
    //     ESP_LOGI(TAG, "HTTP POST Status = %d, content_length = %d",
    //         esp_http_client_get_status_code(client), content_length);
    //     ESP_LOGI(TAG, "recv:%s", local_response_buffer);
    // }
    // else
    // {
    //     ESP_LOGE(TAG, "HTTP POST request failed: %s", esp_err_to_name(err));
    // }

    // cJSON_free((void*)post_data);
    // esp_http_client_cleanup(client);

        // vTaskDelay(1000 / portTICK_PERIOD_MS);
    }
}


