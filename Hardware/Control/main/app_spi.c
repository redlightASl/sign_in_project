#include "app_spi.h"

static const char *SPI_TAG="STM32";

void SPI_init(void)
{
    esp_err_t ret;
    spi_host_device_t STM32_Trans;

    spi_bus_config_t buscfg={
        .miso_io_num = PIN_NUM_MISO,
        .mosi_io_num = PIN_NUM_MOSI,
        .sclk_io_num = PIN_NUM_CLK,
        .quadwp_io_num = -1,
        .quadhd_io_num = -1,
        .max_transfer_sz = 32,
    };

    spi_device_interface_config_t devcfg={
        .command_bits = 0,
        .address_bits = 8,
        .dummy_bits = 0,
        .duty_cycle_pos = 128;// default duty cycle=128 == 50%/50% duty
        .cs_ena_pretrans = 0;// disabled
        .cs_ena_posttrans = 0;// disabled
        .clock_speed_hz = STM32_CLK_FREQ,
        .mode = 0,// SPI Mode-0
        .spics_io_num = STM32_CS_PIN,
        .flags = 0,// diasbled
        .queue_size = 1,
        .pre_cb = NULL,
        .post_cb = NULL,
    };

    //init SPI bus
    ret = spi_bus_initialize(STM32_Trans, &buscfg,1);
    ESP_ERROR_CHECK(ret);

    ret = spi_bus_add_device(STM32_Trans,&devcfg,&spi);
    ESP_ERROR_CHECK(ret);
}

void SendCommand(Command_enum command);
{
    esp_err_t ret;
    spi_transaction_t comand_trans={
        .cmd = CMD_WRITE | (addr & ADDR_MASK),
        .length = 8,
        .flags = SPI_TRANS_USE_TXDATA,
        .tx_data = command,
        .user = ctx,
    };

    ret = spi_device_polling_transmit(ctx->spi,&comand_trans);
    if (ret != ESP_OK)
    {
        ESP_LOGI(SPI_TAG,"SPI Polling trans command failed");
    }
}

void SendMessage(Message_enum message);
{
    esp_err_t ret;
    spi_transaction_t message_trans={
        .cmd = CMD_WRITE | (addr & ADDR_MASK),
        .length = 8,
        .flags = SPI_TRANS_USE_TXDATA,
        .tx_data = message,
        .user = ctx,
    };

    ret = spi_device_polling_transmit(ctx->spi,&message_trans);
    if (ret != ESP_OK)
    {
        ESP_LOGI(SPI_TAG,"SPI Polling trans message failed");
    }
}

void TaskSpiTrans(void* param)
{
    esp_err_t ret;
    spi_transaction_t t;
    memset(&t,0,sizeof(t));
    

    ret=spi_device_polling_transmit()
    if (ret != ESP_OK)
    {
        ESP_LOGI(SPI_TAG,"SPI Polling trans task failed");
    }
}