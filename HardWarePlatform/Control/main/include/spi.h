#ifndef __APP_SPI_H
#define __APP_SPI_H

const char* SPI_TAG="STM32";

//commands sent to stm32
typedef enum ESP32COMMAND
{
	NOP=1,		//空指令，表示ESP32在线
	STOP, 		//暂停当前任务
	RUN,        //继续执行当前任务
	SLEEP,		//进入睡眠模式
}COMMAND;

void SPI_init(void);




#endif