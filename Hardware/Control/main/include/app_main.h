/*
 * Global Settings for ESP32
 * Written by Redlight
 */
#ifndef __APP_MAIN_H
#define __APP_MAIN_H

#define VERSION "1.0.0"
#define DEVICE "ESP_WROOM_32E"

#define TASK_STACK_SIZE 128
#define TASK_PRIORITY 5

#define GPIO_OUTPUT_IO_9 9
#define GPIO_OUTPUT_PIN_SEL ((1ULL<<GPIO_OUTPUT_IO_9)

//commands sent to stm32
typedef enum commandenum
{
	ONLINE    = 	0x01,		// 空指令，表示ESP32在线
	STOP  	  = 	0x02,		// 强制停止当前任务
	SLEEP 	  = 	0x03,		// 进入睡眠模式
	OTA   	  = 	0x04,		// 开始执行OTA升级，后面跟随的就是OTA升级数据
}Command_enum;

//messages sent from stm32
typedef enum messageenum
{
	ONLINE    = 	0x01,		// 空消息，表示STM32在线
	BUSY	  =		0x02,		// 忙碌，表示正在执行运算任务
	DATA	  = 	0x05,		// 数据，表示有数据被传输
	ERROR	  =		0x06,		// 出现严重错误，收到这条消息时 ESP32 发出报警指示
}Message_enum;

//machine state
typedef enum stateenum
{
	IDLE     =		0x00,		// 空闲
	BUSY	 =		0x01,		// 忙碌
	ERROR    =      0x06,		// 出现严重错误，处于该状态时 ESP32 发出报警指示
}State_enum;

xQueueHandle ESP32_State_Queue;
xQueueHandle STM32_State_Queue;

#endif