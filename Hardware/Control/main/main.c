#include "include/main.h"
#include "include/spi.h"
#include "include/camera.h"
#include "include/wifi.h"

extern const char* TAG;

void app_main(void)
{
    esp_err_t ret;//debug logs

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

    //init peripherals
    ESP_ERROR_CHECK(camera_init());
    ESP_ERROR_CHECK(SPI_init());
    ESP_ERROR_CHECK(WIFI_init());

    xTaskCreate();//control Camera
    xTaskCreate();//SPI communication with STM32
    xTaskCreate();//upload results to Server
    xTaskCreate();//listen to Server-Commands
}