/*
* SPI bus configuration and transmission
* 
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
	NOP=1,		// 空指令，表示ESP32在线
	STOP,		// 强制停止当前任务并丢弃所有计算结果
	SLEEP,		// 进入睡眠模式
	OTA,		// 开始执行OTA升级
}Command_enum;

//messages sent from stm32
typedef enum MASTER_MESSAGE
{
	IDLE=1,		// 空闲，表示STM32在线
	BUSY,		// 忙碌，表示正在执行运算任务
	RESULT,		// 汇报结果，此消息发送后会跟随计算得到的数据	
	ERROR,		// 出错
}Message_enum;

//machine state
typedef enum LOCAL_STATE
{
	IDLE=1,		// 空闲
	BUSY,		// 忙碌
	ERROR,		// 出错
}State_enum;




void SPI_init(void);
void SendCommand(Command_enum command);
void SendMessage(Message_enum message);
void TaskSpiTrans(void* param);

#endif