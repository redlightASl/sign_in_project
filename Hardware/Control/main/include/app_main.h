#ifndef __APP_MAIN_H
#define __APP_MAIN_H

#define GPIO_OUTPUT_PIN 12
#define GPIO_OUTPUT_PIN_SEL (1ULL << GPIO_OUTPUT_PIN)

#define HTTP_GET_BUFFER_SIZE 512
#define TIMESTAMP_SIZE 128
#define HTTP_POST_BUFFER_SIZE 2048

typedef enum mes_type {
	ONLINE = 0x00, // 空消息，表示设备在线
	STOP = 0x01, // 强制停机
	SLEEP = 0x02, // 进入睡眠模式
	OTA = 0x03, // 开始执行OTA升级
	DATA = 0x04, // 人脸识别数据
	ERROR = 0x05, // 出现严重错误
} MesageType;

extern SemaphoreHandle_t HTTP_GET_TIMESTAMP_FLAG;

extern QueueHandle_t server_to_spi_Queue;
extern QueueHandle_t spi_to_server_Queue;
extern QueueHandle_t trans_timestamp_Queue;
extern QueueHandle_t trans_fingerprint_Queue;

#endif
