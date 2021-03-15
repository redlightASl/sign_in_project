/**
  ******************************************************************************
  * @file    hash.c
  * @brief   This file provides code for the configuration
  *          of the HASH instances.
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

/* Includes ------------------------------------------------------------------*/
#include "hash.h"

/* USER CODE BEGIN 0 */

/* USER CODE END 0 */

HASH_HandleTypeDef hhash;

/* HASH init function */
void MX_HASH_Init(void)
{

  hhash.Init.DataType = HASH_DATATYPE_32B;
  if (HAL_HASH_Init(&hhash) != HAL_OK)
  {
    Error_Handler();
  }

}

void HAL_HASH_MspInit(HASH_HandleTypeDef* hashHandle)
{

  /* USER CODE BEGIN HASH_MspInit 0 */

  /* USER CODE END HASH_MspInit 0 */
    /* HASH clock enable */
    __HAL_RCC_HASH_CLK_ENABLE();
  /* USER CODE BEGIN HASH_MspInit 1 */

  /* USER CODE END HASH_MspInit 1 */
}

void HAL_HASH_MspDeInit(HASH_HandleTypeDef* hashHandle)
{

  /* USER CODE BEGIN HASH_MspDeInit 0 */

  /* USER CODE END HASH_MspDeInit 0 */
    /* Peripheral clock disable */
    __HAL_RCC_HASH_CLK_DISABLE();
  /* USER CODE BEGIN HASH_MspDeInit 1 */

  /* USER CODE END HASH_MspDeInit 1 */
}

/* USER CODE BEGIN 1 */

/* USER CODE END 1 */

/************************ (C) COPYRIGHT STMicroelectronics *****END OF FILE****/
