#include "app_main.h"

static const char *TAG = "app_main";

void app_main(void)
{
    esp_err_t ret;//debug logs

    //init system
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_ERROR_CHECK(esp_event_loop_init(wifi_event_handler, NULL));
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
}