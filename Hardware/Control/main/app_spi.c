#include "app_spi.h"

static const char *SPI_TAG="STM32";

void SPI_init(void)
{
    esp_err_t ret;

    spi_bus_config_t buscfg={
        .miso_io_num = PIN_NUM_MISO,
        .mosi_io_num = PIN_NUM_MOSI,
        .sclk_io_num = PIN_NUM_CLK,
        .quadwp_io_num = -1,
        .quadhd_io_num = -1,
        .max_transfer_sz = 32,
    };
    //init SPI bus
    ret = spi_bus_initialize(EEPROM_HOST, &buscfg, DMA_CHAN);
    ESP_ERROR_CHECK(ret);
}

void SendCommand(Command_enum command);
{

}

void SendMessage(Message_enum message);
{

}

void TaskSpiTrans(void* param)
{
    
}