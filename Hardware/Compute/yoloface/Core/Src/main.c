/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * @file           : main.c
  * @brief          : Main program body
  ******************************************************************************
  * @attention
  *
  * <h2><center>&copy; Copyright (c) 2020 STMicroelectronics.
  * All rights reserved.</center></h2>
  *
  * This software component is licensed by ST under BSD 3-Clause license,
  * the "License"; You may not use this file except in compliance with the
  * License. You may obtain a copy of the License at:
  *                        opensource.org/licenses/BSD-3-Clause
  *
  ******************************************************************************
  */
/* USER CODE END Header */
/* Includes ------------------------------------------------------------------*/
#include "main.h"
#include "crc.h"
#include "dcmi.h"
#include "dma.h"
#include "i2c.h"
#include "spi.h"
#include "tim.h"
#include "usart.h"
#include "gpio.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <string.h>
#include "camera.h"
#include "lcd.h"

#include "yoloface.h"
#include "yoloface_data.h"

//#include "yoloface_nn.h"
/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */
/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/

/* USER CODE BEGIN PV */
// picture buffer
uint16_t pic[FrameWidth][FrameHeight];
uint32_t DCMI_FrameIsReady;
uint32_t Camera_FPS = 0;

ai_i8 activations[AI_YOLOFACE_DATA_ACTIVATIONS_SIZE];
ai_i8 in_data[AI_YOLOFACE_IN_1_SIZE_BYTES];
ai_i8 out_data[AI_YOLOFACE_OUT_1_SIZE_BYTES];

/* AI buffer IO handlers */
ai_buffer *ai_input;
ai_buffer *ai_output;

// �������������������������
ai_u8 x1, y1, x2, y2;
// yoloface��anchor�ߴ�
int8_t anchors[3][2] = {{9, 14}, {12, 17}, {22, 21}};

/* USER CODE END PV */

/* Private function prototypes -----------------------------------------------*/
void SystemClock_Config(void);
/* USER CODE BEGIN PFP */


/* USER CODE END PFP */

/* Private user code ---------------------------------------------------------*/
/* USER CODE BEGIN 0 */

int fputc(int ch, FILE *stream)
{
    while((USART1->ISR & 0X40) == 0);
    USART1->TDR = (uint8_t) ch;
    return ch;
}

static void MPU_Config(void)
{
	MPU_Region_InitTypeDef MPU_InitStruct = { 0 };

	/* Disables the MPU */
	HAL_MPU_Disable();

	/* Configure the MPU attributes for the QSPI 256MB without instruction access */
	MPU_InitStruct.Enable = MPU_REGION_ENABLE;
	MPU_InitStruct.Number = MPU_REGION_NUMBER0;
	MPU_InitStruct.BaseAddress = QSPI_BASE;
	MPU_InitStruct.Size = MPU_REGION_SIZE_256MB;
	MPU_InitStruct.AccessPermission = MPU_REGION_NO_ACCESS;
	MPU_InitStruct.IsBufferable = MPU_ACCESS_NOT_BUFFERABLE;
	MPU_InitStruct.IsCacheable = MPU_ACCESS_NOT_CACHEABLE;
	MPU_InitStruct.IsShareable = MPU_ACCESS_NOT_SHAREABLE;
	MPU_InitStruct.DisableExec = MPU_INSTRUCTION_ACCESS_DISABLE;
	MPU_InitStruct.TypeExtField = MPU_TEX_LEVEL1;
	MPU_InitStruct.SubRegionDisable = 0x00;
	HAL_MPU_ConfigRegion(&MPU_InitStruct);

	/* Configure the MPU attributes for the QSPI 8MB (QSPI Flash Size) to Cacheable WT */
	MPU_InitStruct.Enable = MPU_REGION_ENABLE;
	MPU_InitStruct.Number = MPU_REGION_NUMBER1;
	MPU_InitStruct.BaseAddress = QSPI_BASE;
	MPU_InitStruct.Size = MPU_REGION_SIZE_8MB;
	MPU_InitStruct.AccessPermission = MPU_REGION_PRIV_RO;
	MPU_InitStruct.IsBufferable = MPU_ACCESS_BUFFERABLE;
	MPU_InitStruct.IsCacheable = MPU_ACCESS_CACHEABLE;
	MPU_InitStruct.IsShareable = MPU_ACCESS_NOT_SHAREABLE;
	MPU_InitStruct.DisableExec = MPU_INSTRUCTION_ACCESS_ENABLE;
	MPU_InitStruct.TypeExtField = MPU_TEX_LEVEL1;
	MPU_InitStruct.SubRegionDisable = 0x00;
	HAL_MPU_ConfigRegion(&MPU_InitStruct);

	/* Setup AXI SRAM in Cacheable WB */
	MPU_InitStruct.Enable = MPU_REGION_ENABLE;
	MPU_InitStruct.BaseAddress = D1_AXISRAM_BASE;
	MPU_InitStruct.Size = MPU_REGION_SIZE_512KB;
	MPU_InitStruct.AccessPermission = MPU_REGION_FULL_ACCESS;
	MPU_InitStruct.IsBufferable = MPU_ACCESS_BUFFERABLE;
	MPU_InitStruct.IsCacheable = MPU_ACCESS_CACHEABLE;
	MPU_InitStruct.IsShareable = MPU_ACCESS_SHAREABLE;
	MPU_InitStruct.Number = MPU_REGION_NUMBER2;
	MPU_InitStruct.TypeExtField = MPU_TEX_LEVEL1;
	MPU_InitStruct.SubRegionDisable = 0x00;
	MPU_InitStruct.DisableExec = MPU_INSTRUCTION_ACCESS_ENABLE;
	HAL_MPU_ConfigRegion(&MPU_InitStruct);

	/* Enables the MPU */
	HAL_MPU_Enable(MPU_PRIVILEGED_DEFAULT);
}

static void CPU_CACHE_Enable(void)
{
	/* Enable I-Cache */
	SCB_EnableICache();

	/* Enable D-Cache */
	SCB_EnableDCache();
}

void LED_Blink(uint32_t Hdelay, uint32_t Ldelay)
{
	HAL_GPIO_WritePin(PE3_GPIO_Port, PE3_Pin, GPIO_PIN_SET);
	HAL_Delay(Hdelay - 1);
	HAL_GPIO_WritePin(PE3_GPIO_Port, PE3_Pin, GPIO_PIN_RESET);
	HAL_Delay(Ldelay - 1);
}

//����sigmoid����
double sigmoid(double x)
{
	double y = 1/(1+expf((-1)*x));
	return y;
}

int32_t ST7735_DrawRect(ST7735_Object_t *pObj, uint32_t Xpos, uint32_t Ypos, uint32_t Width, uint32_t Height, uint32_t Color)
{
	ST7735_DrawHLine(&st7735_pObj,Xpos,Ypos,Width,Color);
	ST7735_DrawVLine(&st7735_pObj,Xpos,Ypos,Height,Color);
	ST7735_DrawHLine(&st7735_pObj,Xpos,Ypos+Height,Width,Color);
	ST7735_DrawVLine(&st7735_pObj,Xpos+Width,Ypos,Height,Color);
}


/* USER CODE END 0 */

/**
  * @brief  The application entry point.
  * @retval int
  */
int main(void)
{
  /* USER CODE BEGIN 1 */
	ai_handle network = AI_HANDLE_NULL;
    ai_error err;
    ai_network_report report;
	const ai_handle acts[] = { activations };

#ifdef W25Qxx
	SCB->VTOR = QSPI_BASE;
#endif
	MPU_Config();
	CPU_CACHE_Enable();

  /* USER CODE END 1 */

  /* Enable I-Cache---------------------------------------------------------*/
  SCB_EnableICache();

  /* Enable D-Cache---------------------------------------------------------*/
  SCB_EnableDCache();

  /* MCU Configuration--------------------------------------------------------*/

  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
  HAL_Init();

  /* USER CODE BEGIN Init */

  /* USER CODE END Init */

  /* Configure the system clock */
  SystemClock_Config();

  /* USER CODE BEGIN SysInit */

  /* USER CODE END SysInit */

  /* Initialize all configured peripherals */
  MX_GPIO_Init();
  MX_DMA_Init();
  MX_DCMI_Init();
  MX_I2C1_Init();
  MX_SPI4_Init();
  MX_TIM1_Init();
  MX_TIM16_Init();
  MX_CRC_Init();
  MX_USART1_UART_Init();
  /* USER CODE BEGIN 2 */
	uint8_t text[32];
	LCD_Init_Device();
#ifdef TFT96
//	Camera_Init_Device(&hi2c1, FRAMESIZE_QQVGA);
	Camera_Init_Device(&hi2c1, FRAMESIZE_YOLO);
#elif TFT18
	Camera_Init_Device(&hi2c1, FRAMESIZE_QQVGA2);
#endif
	sprintf((char*)&text, "Camera id:0x%x   ", hcamera.device_id);
	LCD_ShowString(0, 58, ST7735Ctx.Width, 16, 12, text);
	LED_Blink(5, 500);
	//clean Ypos 58
	ST7735_LCD_Driver.FillRect(&st7735_pObj, 0, 58, ST7735Ctx.Width, 16, BLACK);

	//startup camera
	HAL_DCMI_Start_DMA(&hdcmi, DCMI_MODE_CONTINUOUS, (uint32_t)&pic, FrameWidth * FrameHeight * 2 / 4);
	printf("Camera Start\r\n");
	

    /** @brief Initialize network */
    err = ai_yoloface_create_and_init(&network, acts, NULL);
    if (err.type != AI_ERROR_NONE) 
	{
        printf("ai init_and_create error\n");
        return -1;
    }
    /** @brief {optional} for debug/log purpose */
    if (ai_yoloface_get_report(network, &report) != true) 
	{
        printf("ai get report error\n");
        return -1;
    }
    printf("Model name      : %s\r\n", report.model_name);
    printf("Model signature : %s\r\n", report.model_signature);
    ai_input = &report.inputs[0];
    ai_output = &report.outputs[0];
    printf("input[0] : (%d, %d, %d)\r\n", AI_BUFFER_SHAPE_ELEM(ai_input, AI_SHAPE_HEIGHT),
                                        AI_BUFFER_SHAPE_ELEM(ai_input, AI_SHAPE_WIDTH),
                                        AI_BUFFER_SHAPE_ELEM(ai_input, AI_SHAPE_CHANNEL));
    printf("output[0] : (%d, %d, %d)\r\n", AI_BUFFER_SHAPE_ELEM(ai_output, AI_SHAPE_HEIGHT),
                                         AI_BUFFER_SHAPE_ELEM(ai_output, AI_SHAPE_WIDTH),
                                         AI_BUFFER_SHAPE_ELEM(ai_output, AI_SHAPE_CHANNEL));
	
  /* USER CODE END 2 */

  /* Infinite loop */
  /* USER CODE BEGIN WHILE */
	while (1)
	{
		/** @brief Perform inference */
		ai_i32 n_batch;
		
		int32_t grid_x=0;
		int32_t grid_y=0;
		double x=0;
		double y=0;
		double w=0; 
		double h=0;
		
		if (DCMI_FrameIsReady)
		{
			DCMI_FrameIsReady = 0;

			/* ǰ���� */
			/** @brief Normalize, convert and/or quantize inputs if necessary... */
			for(uint8_t i = 0; i < 56; i++)
			{
				for(uint8_t j = 0; j < 56; j++)
				{
					uint16_t color = (*pic)[j+i*56];
					in_data[(j+i*56)*3] = (ai_i8)((color&0xF800)>>9) - 128;
					in_data[(j+i*56)*3+1] = (ai_i8)((color&0x07E0)>>3) - 128;
					in_data[(j+i*56)*3+2] = (ai_i8)((color&0x001F)<<3) - 128;
				}
			}
			
			HAL_DCMI_Resume(&hdcmi);

			/** @brief Create the AI buffer IO handlers
			 *  @note  ai_inuput/ai_output are already initilaized after the
			 *         ai_network_get_report() call. This is just here to illustrate
			 *         the case where get_report() is not called.
			 */
			ai_input = ai_yoloface_inputs_get(network, NULL);
			ai_output = ai_yoloface_outputs_get(network, NULL);

			/** @brief Set input/output buffer addresses */
			ai_input[0].data = AI_HANDLE_PTR(in_data);
			ai_output[0].data = AI_HANDLE_PTR(out_data);
			
			/** @brief Perform the inference */
			n_batch = ai_yoloface_run(network, &ai_input[0], &ai_output[0]);
			if (n_batch != 1) 
			{
				err = ai_yoloface_get_error(network);
				printf("ai run error %d, %d\n", err.type, err.code);
				return -1;
			}
			
			for(uint8_t i = 0; i < 49; i++)
			{
				for(uint8_t j = 0; j < 3; j++)
				{
					ai_i8 conf = out_data[i*18+j*6+4];
					// 网络输出维度是1*7*7*18
					// 其中18维度包含了每个像素预测的三个锚框，每个锚框对应6个维度，依次为x y w h conf class
					// 当然因为这个网络是单类检测，所以class这一维度没有用
					// 这里的-9是根据网络量化的缩放偏移量计算的，对应的是70%的置信度
					// sigmoid((conf+15)*0.14218327403068542) < 0.7 ==> conf > -9
					if(conf > -9)
					{
						grid_x = i % 7;
						grid_y = (i - grid_x)/7;
						// 这里的15和0.14218327403068542就是网络量化后给出的缩放偏移量
						x = ((double)out_data[i*18+j*6]+15)*0.14218327403068542f;
						y = ((double)out_data[i*18+j*6+1]+15)*0.14218327403068542f;
						w = ((double)out_data[i*18+j*6+2]+15)*0.14218327403068542f;
						h = ((double)out_data[i*18+j*6+3]+15)*0.14218327403068542f;
						printf("Get original num:%lf,%lf,%lf,%lf\r\n",x,y,w,h);
						// 网络下采样三次，缩小了8倍，这里给还原回56*56的尺度
						x = (sigmoid(x)+(double)grid_x) * 8;
						y = (sigmoid(y)+(double)grid_y) * 8;
						w = expf(w) * anchors[j][0];
						h = expf(h) * anchors[j][1];
						
						x1=(ai_u8)(x - (w/2));
						y1=(ai_u8)(y - (h/2));
						x2=(ai_u8)(x + (w/2));
						y2=(ai_u8)(y + (h/2));
						

//						if(x1 < 0)
//							x1 = 0;
//						if(y1 < 0)
//							y1 = 0;
//						if(x2 > 55)
//							x2 = 55;
//						if(y2 > 55)
//							y2 = 55;

						printf("Find Face in [%d, %d]\r\nW: %d,H: %d\r\n\r\n",(ai_i8)x1, (ai_i8)y1, (ai_i8)w, (ai_i8)h);
					}
				}
			}
    /* USER CODE END WHILE */

    /* USER CODE BEGIN 3 */
#ifdef TFT96
//			ST7735_FillRGBRect(&st7735_pObj, 0, 0, (uint8_t*)&pic[20][0], ST7735Ctx.Width, 80);
//			ST7735_FillRGBRect(&st7735_pObj, 0, 0, (uint8_t*)&pic[0][0], ST7735Ctx.Width, 80);
			ST7735_DrawRect(&st7735_pObj, x1, y1, w, h, RED);
			ST7735_FillRGBRect(&st7735_pObj, 0, 0, (uint8_t*)&pic[0][0], 56, 56);

			
//			ST7735_FillRGBRect(&st7735_pObj, 0, 0, (uint8_t*)&pic[0][0], 56, 56);
//			ST7735_DrawRect(&st7735_pObj, x1, y1, w, h, RED);
#elif TFT18
			ST7735_FillRGBRect(&st7735_pObj, 0, 0, (uint8_t*)&pic[0][0], ST7735Ctx.Width, ST7735Ctx.Height);
#endif
			
//			printf("%dFPS\r\n", Camera_FPS);
//			LED_Blink(1, 1);
		}
	}
  /* USER CODE END 3 */
}

/**
  * @brief System Clock Configuration
  * @retval None
  */
void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};

  /** Supply configuration update enable
  */
  HAL_PWREx_ConfigSupply(PWR_LDO_SUPPLY);
  /** Configure the main internal regulator output voltage
  */
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE0);

  while(!__HAL_PWR_GET_FLAG(PWR_FLAG_VOSRDY)) {}
  /** Initializes the RCC Oscillators according to the specified parameters
  * in the RCC_OscInitTypeDef structure.
  */
  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI48|RCC_OSCILLATORTYPE_HSE;
  RCC_OscInitStruct.HSEState = RCC_HSE_ON;
  RCC_OscInitStruct.HSI48State = RCC_HSI48_ON;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSE;
  RCC_OscInitStruct.PLL.PLLM = 5;
  RCC_OscInitStruct.PLL.PLLN = 192;
  RCC_OscInitStruct.PLL.PLLP = 2;
  RCC_OscInitStruct.PLL.PLLQ = 20;
  RCC_OscInitStruct.PLL.PLLR = 2;
  RCC_OscInitStruct.PLL.PLLRGE = RCC_PLL1VCIRANGE_2;
  RCC_OscInitStruct.PLL.PLLVCOSEL = RCC_PLL1VCOWIDE;
  RCC_OscInitStruct.PLL.PLLFRACN = 0;
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }
  /** Initializes the CPU, AHB and APB buses clocks
  */
  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2
                              |RCC_CLOCKTYPE_D3PCLK1|RCC_CLOCKTYPE_D1PCLK1;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.SYSCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_HCLK_DIV2;
  RCC_ClkInitStruct.APB3CLKDivider = RCC_APB3_DIV2;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_APB1_DIV2;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_APB2_DIV2;
  RCC_ClkInitStruct.APB4CLKDivider = RCC_APB4_DIV2;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_4) != HAL_OK)
  {
    Error_Handler();
  }
  HAL_RCC_MCOConfig(RCC_MCO1, RCC_MCO1SOURCE_HSI48, RCC_MCODIV_4);
}

/* USER CODE BEGIN 4 */

void HAL_DCMI_FrameEventCallback(DCMI_HandleTypeDef* hdcmi)
{
	static uint32_t count = 0, tick = 0;

	if (HAL_GetTick() - tick >= 1000)
	{
		tick = HAL_GetTick();
		Camera_FPS = count;
		count = 0;
	}
	count++;

	DCMI_FrameIsReady = 1;
	HAL_DCMI_Suspend(hdcmi);
}

/* USER CODE END 4 */

/**
  * @brief  This function is executed in case of error occurrence.
  * @retval None
  */
void Error_Handler(void)
{
  /* USER CODE BEGIN Error_Handler_Debug */
	/* User can add his own implementation to report the HAL error return state */
	while (1)
	{
		LED_Blink(5, 250);
	}
  /* USER CODE END Error_Handler_Debug */
}

#ifdef  USE_FULL_ASSERT
/**
  * @brief  Reports the name of the source file and the source line number
  *         where the assert_param error has occurred.
  * @param  file: pointer to the source file name
  * @param  line: assert_param error line source number
  * @retval None
  */
void assert_failed(uint8_t *file, uint32_t line)
{
  /* USER CODE BEGIN 6 */
	/* User can add his own implementation to report the file name and line number,
	   tex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
  /* USER CODE END 6 */
}
#endif /* USE_FULL_ASSERT */

