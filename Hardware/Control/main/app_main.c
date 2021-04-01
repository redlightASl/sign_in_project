#include "app_main.h"
#include "app_spi.h"
#include "app_wifi.h"

static const char* TAG=DEVICE;

void app_main(void)
{
    esp_err_t ret;//debug logs
    size_t total=0;//SPIFFS state
    size_t used=0;//SPIFFS state

    //init system
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_LOGI(TAG, "event_loop init success\n");

    //init NVS
    ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }
    ESP_ERROR_CHECK(ret);
    ESP_LOGI(TAG, "%s init NVS finished\n", __func__);

    //init SPIFFS
    esp_vfs_spiffs_conf_t spiffs_conf = {
        .base_path = "/root",
        .partition_label = NULL,
        .max_files = 5,
        .format_if_mount_failed = true
    };
    ESP_ERROR_CHECK(esp_vfs_spiffs_register(&spiffs_conf));
    ret = esp_spiffs_info(NULL, &total, &used);
    if (ret != ESP_OK)
        ESP_LOGE(TAG, "Failed to get SPIFFS partition information (%s)", esp_err_to_name(ret));
    else
        ESP_LOGI(TAG, "Partition size: total: %d, used: %d", total, used);

    //init peripherals
    ESP_ERROR_CHECK(SPI_init());
    ESP_ERROR_CHECK(WIFI_init());

    xTaskCreate(TaskSpiTrans,"SPI",TASK_STACK_SIZE,"SPI",TASK_PRIORITY,NULL);//SPI communication with STM32
    xTaskCreate(TaskUpload,"upload",TASK_STACK_SIZE,"upload",TASK_PRIORITY,NULL);//upload results to Server
    xTaskCreate(TaskListen,"listen",TASK_STACK_SIZE,"listen",TASK_PRIORITY,NULL);//listen to Server-Commands

    while (1)
    {
        vTaskDelay(1000/portTICK_PERIOD_MS);
    }
    vTaskDelete();
}