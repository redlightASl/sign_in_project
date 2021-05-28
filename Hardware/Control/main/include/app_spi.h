/*
* SPI bus configuration and transmission
* ESP32 只会向 STM32 发送指令
* STM32 只会向 ESP32 发送数据
* Written by Redlight
*/
#ifndef __APP_SPI_H
#define __APP_SPI_H

/* SPI0 HSPI for master mode */
#define PIN_NUM_MISO 	12
#define PIN_NUM_MOSI 	2
#define PIN_NUM_CLK 	0
#define STM32_CS_PIN 	19

#define STM32_CLK_FREQ 40*1000*1000 //40MHz

void SPI_init(void);
void SendCommand(Command_enum command);
void SendMessage(Message_enum message);
void TaskSpiTrans(void* param);
void SubTaskOTA(void* param);

#endif