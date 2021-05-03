/*
* SPI bus configuration and transmission
* ESP32 只会向 STM32 发送指令
* STM32 只会向 ESP32 发送数据
*/
#ifndef __APP_SPI_H
#define __APP_SPI_H

#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "freertos/event_groups.h"
#include "driver/spi_common.h"
#include "driver/spi_master.h"
#include "esp_system.h"
#include "esp_event.h"
#include "esp_log.h"
#include "sys/param.h"

#define PIN_NUM_MISO 	32
#define PIN_NUM_MOSI 	33
#define PIN_NUM_CLK 	21
#define STM32_CS_PIN 	-1

#define STM32_CLK_FREQ 40*1000*1000 //40MHz

//commands sent to stm32
typedef enum MASTER_COMMAND
{
	ONLINE    = 	0x01,		// 空指令，表示ESP32在线
	STOP  	  = 	0x02,		// 强制停止当前任务
	SLEEP 	  = 	0x03,		// 进入睡眠模式
	OTA   	  = 	0x04,		// 开始执行OTA升级，后面跟随的就是OTA升级数据
}Command_enum;

//messages sent from stm32
typedef enum MASTER_MESSAGE
{
	ONLINE    = 	0x01,		// 空指令，表示STM32在线
	BUSY	  =		0x02,		// 忙碌，表示正在执行运算任务
	ERROR	  =		0x06,		// 出现严重错误，收到这条消息时 ESP32 发出报警指示
}Message_enum;

//machine state
typedef enum LOCAL_STATE
{
	IDLE     =		0x00,		// 空闲
	BUSY	 =		0x01,		// 忙碌
	ERROR    =      0x06,		// 出现严重错误，处于该状态时 ESP32 发出报警指示
}State_enum;

void SPI_init(void);
void SendCommand(Command_enum command);
void SendMessage(Message_enum message);
void TaskSpiTrans(void* param);

#endif