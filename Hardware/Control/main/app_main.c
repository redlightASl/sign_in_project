#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "sys/param.h"
#include "driver/gpio.h"

#include "app_main.h"
#include "app_spi.h"
#include "app_wifi.h"

static const char* TAG=DEVICE;

void app_main(void)
{
    State_enum* ESP32_STATE; //state-pointer of ESP32
    esp_err_t ret;//debug logs
    size_t total=0;//SPIFFS state
    size_t used=0;//SPIFFS state

    /* init system event loop and Queue*/
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_LOGI(TAG, "event_loop init success");

    xQueueHandle ESP32_State_Queue = xQueueCreate(10, sizeof(State_enum)); //create a queue to handle ESP32 State
    if(ESP32_State_Queue == 0)
        ESP_LOGE(TAG, "Failed to create Queue of ESP32_STATE");
    xQueueHandle STM32_State_Queue = xQueueCreate(10, sizeof(Message_enum)); //create a queue to handle STM32 State
    if(STM32_State_Queue == 0)
        ESP_LOGE(TAG, "Failed to create Queue of STM32_STATE");

    /* init NVS */
    ret = nvs_flash_init();
    if (ret == ESP_ERR_NVS_NO_FREE_PAGES || ret == ESP_ERR_NVS_NEW_VERSION_FOUND)
    {
        ESP_ERROR_CHECK(nvs_flash_erase());
        ret = nvs_flash_init();
    }
    ESP_ERROR_CHECK(ret);
    ESP_LOGI(TAG, "%s init NVS finished", __func__);

    /* init SPIFFS */
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

    /* init GPIO peripherals without interrupt */
    gpio_config_t LED_io_conf;
    LED_io_conf.intr_type = GPIO_PIN_INTR_DISABLE; //disable interrupt
    LED_io_conf.mode = GPIO_MODE_OUTPUT; //output mode
    LED_io_conf.pin_bit_mask = GPIO_OUTPUT_PIN_SEL; //bit mask of the pins
    LED_io_conf.pull_down_en = 0;
    LED_io_conf.pull_up_en = 1; //enable pull-up mode
    gpio_config(&LED_io_conf);
    ESP_LOGI(TAG, "%s init GPIO finished", __func__);

    /* init camera periperals */
    camera_start();

    /* init SPI peripherals */
    //ESP_ERROR_CHECK(SPI_init());

    /* init OLED peripherals */
    /* NOT DECIDED */

    /* init WiFi peripherals */
    //ESP_ERROR_CHECK(ESP_wifi_init());
    //ESP_LOGI(TAG, "%s init all the peripherals finished", __func__);
    /* Create Tasks */
    xTaskCreate(TaskSpiTrans,"SPI",TASK_STACK_SIZE,"SPI",TASK_PRIORITY,NULL);//SPI communication with STM32
    xTaskCreate(TaskUpload,"upload",TASK_STACK_SIZE,"upload",TASK_PRIORITY,NULL);//upload results to Server
    xTaskCreate(TaskListen,"listen",TASK_STACK_SIZE,"listen",TASK_PRIORITY,NULL);//listen to Server-Commands
    ESP_LOGI(TAG, "%s Tasks were created", __func__);
    ESP_LOGI("System Running!");

    while (1)
    {
        xQueueReceive(ESP32_State_Queue, &ESP32_STATE, (TickType_t)0)
        if(*STATE == ERROR) //error
        {
            gpio_set_level(GPIO_OUTPUT_IO_9, 1); //lightup LED
        }
        else if(*STATE == IDLE) //idle
        {
            gpio_set_level(GPIO_OUTPUT_IO_9, 0); //extinguish LED
        }
        else if(*STATE == BUSY) //busy
        {
            vTaskDelay(10000/portTICK_PERIOD_MS);
            continue;
        }
        else
            vTaskDelay(1000/portTICK_PERIOD_MS);
    }
    vTaskDelete();
}