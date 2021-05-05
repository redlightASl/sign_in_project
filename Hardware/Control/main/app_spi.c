#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "driver/spi_common.h"
#include "driver/spi_master.h"
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "sys/param.h"

#include "app_spi.h"
#include "app_main.h"

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

Message_enum check_message_type(spi_transaction_t t)
{

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

/*
 * if received a SPI message about faceid,put a event to UploadTask
 * else check the message and eal with it
 */
void TaskSpiTrans(void* param)
{
    esp_err_t ret;
    Message_enum* MESSAGE; //state-pointer of STM32
    State_enum* ESP32_STATE; //state-pointer of ESP32
    Command_enum COMMAND;
    
    spi_transaction_t t;
    memset(&t,0,sizeof(t));

    while (1)
    {
        ret=spi_device_polling_transmit() //轮询收取SPI消息
        if (ret != ESP_OK)
        {
            ESP_LOGI(SPI_TAG,"SPI Polling trans task failed");
        }

        //检查SPI消息类型
        *MESSAGE = check_message_type(t.rx_data);
        //如果是人脸识别数据，直接将其挂在消息队列传输给wifi
        xQueueSend(STM32_State_Queue, (void *)&MESSAGE, (TickType_t)0);
        //如果是忙碌，延迟后再次收取SPI消息
        xQueueSend(STM32_State_Queue, (void *)&MESSAGE, (TickType_t)0);
        //如果是空消息，忽略


        //如果收到休眠事件，则发送进入睡眠模式指令到STM32

        //如果收到OTA事件，则发送执行开始执行OTA升级指令
        xTaskCreate(SubTaskOTA,"OTA",TASK_STACK_SIZE,"OTA",TASK_PRIORITY,NULL);
        //如果收到强制停止事件，则发送强制停止当前任务指令并将当前状态置为忙碌
        *ESP32_STATE = BUSY;
    }
    vTaskDelete();
}

void SubTaskOTA(void* param)
{

}