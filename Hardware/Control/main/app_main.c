#include <string.h>
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/task.h"
#include "esp_system.h"
#include "esp_wifi.h"
#include "driver/gpio.h"
#include "esp_event.h"
#include "esp_log.h"
#include "nvs_flash.h"

#include "lwip/err.h"
#include "lwip/sockets.h"
#include "lwip/sys.h"
#include "lwip/netdb.h"
#include "lwip/dns.h"
#include "cJSON.h"

#include "protocol_examples_common.h"
#include "app_web.h"
#include "app_spi.h"

#include "app_main.h"

static const char* TAG = "esp_IoT";

SemaphoreHandle_t HTTP_GET_TIMESTAMP_FLAG = NULL;

QueueHandle_t server_to_spi_Queue = NULL;
QueueHandle_t spi_to_server_Queue = NULL;
QueueHandle_t trans_timestamp_Queue = NULL;
QueueHandle_t trans_fingerprint_Queue = NULL;

static void SOC_init(void);
static void NVS_init(void);
static void GPIO_init(void);
void TaskRun(void* param);

void app_main(void)
{
    SOC_init();
    NVS_init();
    GPIO_init();
    SPI_init();

    ESP_ERROR_CHECK(esp_netif_init());
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_ERROR_CHECK(example_connect());

    HTTP_GET_TIMESTAMP_FLAG = xSemaphoreCreateBinary();
    if (HTTP_GET_TIMESTAMP_FLAG == NULL)
    {
        ESP_LOGE(TAG, "Cannot create Semaphore HTTP_GET_TIMESTAMP_FLAG");
    }
    xSemaphoreGive(HTTP_GET_TIMESTAMP_FLAG);

    server_to_spi_Queue = xQueueCreate(10, TIMESTAMP_SIZE * sizeof(char));
    if (server_to_spi_Queue == NULL)
    {
        ESP_LOGE(TAG, "Cannot create Queue server_to_spi_Queue");
    }
    spi_to_server_Queue = xQueueCreate(10, TIMESTAMP_SIZE * sizeof(char));
    if (spi_to_server_Queue == NULL)
    {
        ESP_LOGE(TAG, "Cannot create Queue spi_to_server_Queue");
    }
    trans_timestamp_Queue = xQueueCreate(10, TIMESTAMP_SIZE * sizeof(char));
    if (trans_timestamp_Queue == NULL)
    {
        ESP_LOGE(TAG, "Cannot create Queue trans_timestamp_Queue");
    }
    trans_fingerprint_Queue = xQueueCreate(10, TIMESTAMP_SIZE * sizeof(char));
    if (trans_fingerprint_Queue == NULL)
    {
        ESP_LOGE(TAG, "Cannot create Queue trans_fingerprint_Queue");
    }

    xTaskCreate(&SPI_trans_start, "SPI_trans_start", 4096, NULL, 5, NULL);
    xTaskCreate(&TaskRun, "TaskRun", 8192, NULL, 5, NULL);
    xTaskCreate(&Get_timestamp_task, "http_get_task", 8192, NULL, 5, NULL);
    xTaskCreate(&Post_data_task, "Post_data_task", 8192, NULL, 5, NULL);
}

/**
 * @brief Run-time check task
 * @param  param            NULL
 */
void TaskRun(void* param)
{
    char message_source[TIMESTAMP_SIZE] = { 0 };
    while (1)
    {
        xQueueReceive(spi_to_server_Queue, &message_source, 0); //Receive data from stm32
        char message_type = message_source[0];
        xQueueSend(trans_fingerprint_Queue, &message_source, 0); //Send data to server
        memset(message_source, 0, TIMESTAMP_SIZE * sizeof(char));
        // switch (message_type)
        // {
        // case: ONLINE
        //     gpio_set_level(GPIO_OUTPUT_PIN, 0);
        //     vTaskDelay(1000 / portTICK_PERIOD_MS);
        //     gpio_set_level(GPIO_OUTPUT_PIN, 1);
        //     vTaskDelay(1000 / portTICK_PERIOD_MS);
        //     break;
        // case: STOP //error
        //     message_type = ERROR;
        //     break;
        // case: SLEEP //error
        //     message_type = ERROR;
        //     break;
        // case: OTA //error
        //     message_type = ERROR;
        //     break;
        // case: DATA
        //     message_type = ONLINE;
        //     break;
        // case: ERROR //error
        //     gpio_set_level(GPIO_OUTPUT_PIN, 0);
        //     ESP_LOGE(TAG, "Error happened\r\n");
        //     fflush(stdout);
        //     esp_restart();
        //     break;
        // default:
        //     message_type = ERROR;
        //     break;
        // }
    }
    printf("Error in running, Restarting now.\n");
    fflush(stdout);
    esp_restart();
}

/**
 * @brief init GPIO peripherals without interrupt
 */
static void GPIO_init(void)
{
    gpio_config_t LED_io_conf;
    LED_io_conf.intr_type = GPIO_PIN_INTR_DISABLE;
    LED_io_conf.mode = GPIO_MODE_OUTPUT;
    LED_io_conf.pin_bit_mask = GPIO_OUTPUT_PIN_SEL;
    LED_io_conf.pull_down_en = 0;
    LED_io_conf.pull_up_en = 1;
    gpio_config(&LED_io_conf);
    ESP_LOGI(TAG, "%s init GPIO finished", __func__);
}

/**
 * @brief Check System-on-Chip
 */
static void SOC_init(void)
{
    esp_chip_info_t chip_info;
    esp_chip_info(&chip_info);
    printf("This is ESP32 chip with %d CPU cores, WiFi%s%s, ",
        chip_info.cores,
        (chip_info.features & CHIP_FEATURE_BT) ? "/BT" : "",
        (chip_info.features & CHIP_FEATURE_BLE) ? "/BLE" : "");
    printf("silicon revision %d, ", chip_info.revision);
    printf("%dMB %s flash\n", spi_flash_get_chip_size() / (1024 * 1024),
        (chip_info.features & CHIP_FEATURE_EMB_FLASH) ? "embedded" : "external");
    printf("cJSON Version:%s\n", cJSON_Version());
}

/**
 * @brief Init NVS Flash
 */
static void NVS_init(void)
{
    esp_err_t ret;
    ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }
    ESP_ERROR_CHECK(ret);
    ESP_LOGI(TAG, "%s init NVS finished", __func__);
}

