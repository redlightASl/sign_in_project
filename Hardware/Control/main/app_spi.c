#include <stdio.h>
#include <stdlib.h>
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

//未完成的检查STM32消息类型函数
Message_enum check_message_type(spi_transaction_t my_t)
{
    
}

//未完成的发送指令函数
void SendCommand(Command_enum command,spi_transaction_t comand_trans);
{
    esp_err_t ret;
    spi_transaction_t comand_trans={
        .cmd = CMD_WRITE | (addr & ADDR_MASK),
        .length = 8,
        .flags = SPI_TRANS_USE_TXDATA,
        .tx_data = command,
        .user = ctx,
    };

    ret = spi_device_transmit(ctx->spi,&comand_trans);
    if (ret != ESP_OK)
    {
        ESP_LOGI(SPI_TAG,"SPI sending command failed");
    }
}

/*
 * if received a SPI message about faceid,put a event to UploadTask
 * else check the message and deal with it
 */
void TaskSpiTrans(void* param)
{
    esp_err_t ret;
    spi_device_handle_t STM32_Trans;
    spi_transaction_t t;
    Message_enum message_type;
    Message_enum* MESSAGE; //state-pointer of STM32
    State_enum* ESP32_STATE; //state-pointer of ESP32
    Command_enum COMMAND;

    char send_message_buffer[64] = {0}; //send buffer
    char receive_message_buffer[64] = {0}; //receive buffer

    //配置SPI总线
    spi_bus_config_t buscfg={
        .miso_io_num = PIN_NUM_MISO,
        .mosi_io_num = PIN_NUM_MOSI,
        .sclk_io_num = PIN_NUM_CLK,
        .quadwp_io_num = -1,
        .quadhd_io_num = -1,
    };

    //配置SPI设备信息
    spi_device_interface_config_t devcfg={
        .command_bits = 0,
        .address_bits = 8,
        .dummy_bits = 0,
        .clock_speed_hz=STM32_CLK_FREQ,
        .duty_cycle_pos = 128;// default duty cycle=128 == 50%/50% duty
        .cs_ena_pretrans = 0;// disabled
        .cs_ena_posttrans = 0;// disabled
        .mode = 0,// SPI Mode-0
        .spics_io_num = STM32_CS_PIN,
        .queue_size = 3,
    };

    //init data buffers
    memset(&t, 0, sizeof(t));
    t.length=sizeof(sendbuf)*8;
    t.tx_buffer=send_message_buffer;
    t.rx_buffer=receive_message_buffer;

    //init SPI bus and mount the device
    ret=spi_bus_initialize(SENDER_HOST, &buscfg, DMA_CHAN);
    ESP_ERROR_CHECK(ret==ESP_OK);
    ret=spi_bus_add_device(SENDER_HOST, &devcfg, &STM32_Trans);
    ESP_ERROR_CHECK(ret==ESP_OK);

    while (1)
    {
        /* 处理STM32发来消息的状态机 */
        ret = spi_device_transmit(handle, &t); //收取SPI消息
        if (ret != ESP_OK)
        {
            ESP_LOGI(SPI_TAG,"SPI trans task failed");
        }

        //检查SPI消息类型
        message_type = check_message_type(t);
        
        if(message_type == DATA)//如果是人脸识别数据，将其挂在消息队列传输给wifi
        {
            
            dump(); //销毁缓存区的消息
        }
        else if(message_type == BUSY)//如果是忙碌，延迟后再次收取SPI消息
        {
            
            dump();
            vTaskDelay(10000/portTICK_PERIOD_MS);
        }
        else if(message_type == ONLINE)//如果是空消息，忽略
        {
            dump();
            vTaskDelay(1000/portTICK_PERIOD_MS);
            continue;
        }
        else if(message_type == ERROR)//如果是出错消息，紧急停机并通过外设提示协处理器出错
        {
            

            vTaskDelay(1000/portTICK_PERIOD_MS);
        }
        else//其他情况忽略
        {
            vTaskDelay(1000/portTICK_PERIOD_MS);
        }

        /* 处理网络命令的状态机 */
        //收取来自其他任务的SPI事件
        if(event_spi_trans == SLEEP)//如果收到休眠事件，则发送进入睡眠模式指令到STM32
        {

        }
        else if(event_spi_trans == STOP)//如果收到强制停止事件，则发送强制停止当前任务指令并将当前状态置为忙碌
        {


        }
        else if(event_spi_trans == OTA)//如果收到OTA事件，则发送OTA指令和数据
        {
            SubTaskOTA();
        }
        else//其他情况忽略
        {
            vTaskDelay(1000/portTICK_PERIOD_MS);
        }
    }
    vTaskDelete();
}

void dump(void)
{

}

void SubTaskOTA(void* param)
{

}