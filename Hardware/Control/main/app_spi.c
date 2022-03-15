#include <string.h>
#include <stdlib.h>
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/queue.h"

#include "spi_bus.h"

#include "app_spi.h"
#include "app_main.h"

static const char* TAG = "SPI_Master";

static spi_bus_handle_t spi_bus_handle = NULL;
static spi_bus_device_handle_t spi_device_handle = NULL;

static uint8_t spi_command_buffer[SIZE_OF_SPI_BUFFER] = { 0 }; //send buffer
static uint8_t spi_message_buffer[SIZE_OF_SPI_BUFFER] = { 0 }; //receive buffer

void SPI_init(void)
{
    do
    {
        spi_config_t spi_bus_conf = {
            .miso_io_num = PIN_NUM_MISO,
            .mosi_io_num = PIN_NUM_MOSI,
            .sclk_io_num = PIN_NUM_CLK,
        }; // spi_bus configurations

        spi_device_config_t spi_device_conf = {
            .cs_io_num = STM32_CS_PIN,
            .mode = 0,
            .clock_speed_hz = STM32_CLK_FREQ,
        }; // spi_device configurations

        //create SpiDevice Object
        spi_bus_handle = spi_bus_create(SPI2_HOST, &spi_bus_conf);
        spi_device_handle = spi_bus_device_create(spi_bus_handle, &spi_device_conf);
    } while (spi_device_handle == NULL);

    ESP_LOGI(TAG, "SPI trans init successfull!");
}

void SPI_trans_start(void* pvParameters)
{
    while (1)
    {
        xQueueReceive(server_to_spi_Queue, spi_command_buffer, 0);
        spi_bus_transfer_bytes(spi_device_handle, spi_command_buffer, spi_message_buffer, TIMESTAMP_SIZE);
        ESP_LOGI(TAG, "Sent spi message: %s", spi_command_buffer);
        ESP_LOGI(TAG, "Got spi message: %s", spi_message_buffer);
        xQueueSend(spi_to_server_Queue, spi_message_buffer, 0);
        vTaskDelay(1000 / portTICK_PERIOD_MS);
    }
    spi_bus_device_delete(&spi_device_handle);
    spi_bus_delete(&spi_bus_handle);
}