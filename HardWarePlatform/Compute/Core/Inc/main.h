/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.h
  * @brief          : Header for main.c file.
  *                   This file contains the common defines of the application.
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2021 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/*
stm32����滮
�ֳ� ͼ���� ���ݴ��� ״̬���� �������� �ĸ�����
ͼ���������ǻ�������ֻҪ�����������͹����ͻ����С�
ͼ����������������ݷ��ͣ����������esp32
���ͽ�����������������ִ��led��˸��ʾ�豸��������
�������м��ִ��״̬���ƣ���SPI��FIFO����ȡ�������

*/



/* USER CODE END Header */

/* Define to prevent recursive inclusion -------------------------------------*/
#ifndef __MAIN_H
#define __MAIN_H

#ifdef __cplusplus
extern "C" {
#endif

/* Includes ------------------------------------------------------------------*/
#include "stm32h7xx_hal.h"
#include "stm32h7xx_hal.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */

/* USER CODE END Includes */

/* Exported types ------------------------------------------------------------*/
/* USER CODE BEGIN ET */

/* USER CODE END ET */

/* Exported constants --------------------------------------------------------*/
/* USER CODE BEGIN EC */

/* USER CODE END EC */

/* Exported macro ------------------------------------------------------------*/
/* USER CODE BEGIN EM */

/* USER CODE END EM */

/* Exported functions prototypes ---------------------------------------------*/
void Error_Handler(void);

/* USER CODE BEGIN EFP */
//��ǵ�ǰstm32����״̬
typedef enum STM32_STATE
{
	BUSY=1,			//���ڴ���ͼ��
	DONE,				//��ǰ�������
	RESTRANS,		//���ڴ���ͼ��
	SPITRANS,		//���ڴ�������
	UNSTABLE,		//���ȶ�״̬
}CURRENT_STATE;
CURRENT_STATE stm32_state_t=UNSTABLE;

char spi_information[64];

/* USER CODE END EFP */

/* Private defines -----------------------------------------------------------*/
/* USER CODE BEGIN Private defines */
void TaskSPI(void);
void TaskRecognize(void);
void TaskRoute(void);
void TaskTransmit(void);
/* USER CODE END Private defines */

#ifdef __cplusplus
}
#endif

#endif /* __MAIN_H */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
