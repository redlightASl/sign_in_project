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
stm32任务规划
分成 图像处理 数据传输 状态控制 例行任务 四个部分
图像处理任务是基本任务，只要有运算任务发送过来就会运行。
图像处理结束后会进行数据发送，将结果返回esp32
发送结束后会进行例行任务，执行led闪烁表示设备正在运行
在任务中间会执行状态控制，从SPI的FIFO中收取控制命令，

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
//标记当前stm32工作状态
typedef enum STM32_STATE
{
	BUSY=1,			//正在处理图像
	DONE,				//当前任务完成
	RESTRANS,		//正在传输图像
	SPITRANS,		//正在传输数据
	UNSTABLE,		//不稳定状态
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
