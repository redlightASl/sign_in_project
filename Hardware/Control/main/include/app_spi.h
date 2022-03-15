#ifndef __APP_SPI_H
#define __APP_SPI_H

/* SPI2 HSPI for master mode */
#define PIN_NUM_MISO 	19
#define PIN_NUM_MOSI 	23
#define PIN_NUM_CLK 	18
#define STM32_CS_PIN 	19

#define STM32_CLK_FREQ  20*1000*1000 //20MHz

#define SIZE_OF_SPI_BUFFER 64

#ifdef CONFIG_IDF_TARGET_ESP32
#define SENDER_HOST HSPI_HOST
#define DMA_CHAN 2
#endif

void SPI_init(void);
void SPI_trans_start(void* pvParameters);
#endif
