# 原理图与PCB

使用AltiumDesigner绘制

本项目的LAYOUT与原理图暂时不开源（其实是不确定要不要单独打板）

可使用ESP32和STM32对应的最小系统板制作项目的电路部分

# 硬件配置

下面是硬件平台PCB与设备规格的配置描述

本项目既可使用PCB制作符合需求的样品，也可以使用开发板进行验证开发

## 基本需求

1. STM32H750VBT6开发板/核心板

2. ESP32-WROOM-32系列模组或开发板

## 引脚配置

### ESP32-WROOM-32

1. SPI：与协处理器通信

使用SPI0 **HSPI**配置为标准SPI主模式，**仅使用下面的SPICLK、SPICS0、SPIMISO、SPIMOSI**，列出所有SPI引脚供参考

| 引脚名 | SPICS0 | SPI_CLK | SPI_MISO | SPI_MOSI |
| ------ | ------ | ------- | -------- | -------- |
| 编号   | 19     | 0       | 2        | 12       |


2. UART：执行OTA升级

| 引脚名 | U0RXD | U0TXD |
| ------ | ----- | ----- |
| 编号   | 40    | 41    |


2. 摄像头接口

采用ESP-WHO框架下的ESP-EYE开发板摄像头接口实现

| 引脚名         | 对应摄像头引脚 | 编号            |
| -------------- | -------------- | --------------- |
| PWDN_GPIO_NUM  | PWDN           | 无（-1）        |
| RESET_GPIO_NUM | RESET          | 无（-1）        |
| XCLK_GPIO_NUM  | XMCLK          | 4               |
| SIOD_GPIO_NUM  | IIC_SDA        | 18              |
| SIOC_GPIO_NUM  | IIC_SCL        | 23              |
| Y9_GPIO_NUM    | Y9             | S_VP(SENSOR_VP) |
| Y8_GPIO_NUM    | Y8             | 37              |
| Y7_GPIO_NUM    | Y7             | 38              |
| Y6_GPIO_NUM    | Y6             | S_VN(SENSOR_VN) |
| Y5_GPIO_NUM    | Y5             | 35              |
| Y4_GPIO_NUM    | Y4             | 14              |
| Y3_GPIO_NUM    | Y3             | 13              |
| Y2_GPIO_NUM    | Y2             | 34              |
| VSYNC_GPIO_NUM | VSYNC          | 5               |
| HREF_GPIO_NUM  | HREF           | 27              |
| PCLK_GPIO_NUM  | PCLK           | 25              |

也可以采用STM32H750VBT6的DCMI接口实现

| 功能        | 对应引脚 |
| ----------- | -------- |
| DCMI_HSYNC  | PA4      |
| DCMI_PIXCLK | PA6      |
| DCMI_XCLK   |          |
| DCMI_SDA    |          |
| DCMI_SCL    |          |
| DCMI_VSYNC  | PB7      |
| DCMI_D6     | PE5      |
| DCMI_D7     | PE6      |
| DCMI_D0     | PC6      |
| DCMI_D1     | PC7      |
| DCMI_D2     | PC8      |
| DCMI_D3     | PC9      |
| DCMI_D4     | PE4      |
| DCMI_D5     | PD3      |
| DCMI_RESET  |          |


3. 供电

![image-20210224011040081](F:\Git_repository\sign_in_project\Hardware\Hardware\README.assets\image-20210224011040081.png)

使用独立的5V转3.3V LDO提供3.3V工作电压，并在模组上加装散热片保证工作温度不高于85℃，也可以选用高温版本的ESP32模组


4. 烧录接口

使用ESP32-DevKitS将ESP32模组烧录完成后焊接到PCB上，不预留烧录接口、不允许二次烧录，考虑使用官方提供的加密API进一步保证安全性

* IO0用于SPI，不允许作为烧录BOOT跳线

5. 报警指示LED

使用Pin21连接LED阳极，GPIO高电平时触发

### STM32H750VBT6

1. SPI：与控制器通信

使用SPI1配置为标准SPI从模式，关闭NSS输入

| 引脚名 | SPI1_SCK | SPI1_MISO | SPI1_MOSI |
| ------ | -------- | --------- | --------- |
| 编号   | PA5      | PB4       | PA7       |

2. UART：执行OTA升级

使用USART1，配置为IAP模式并写入IAP引导程序

| 引脚名 | US1Tx | US1Rx |
| ------ | ----- | ----- |
| 编号   | PB14  | PA10  |


2. 供电

使用独立的3.3V LDO供电，不单独提供备份时钟域供电，但出于安全性考虑加入应急断电保护电路，以保证断电情况下设备可以将最后得出数据发送给ESP32且能够接收来自ESP32的休眠指令

![image-20210224015156411](C:\Users\NH55\AppData\Roaming\Typora\typora-user-images\image-20210224015156411.png)

3. 烧录接口

本项目使用UART对STM32执行OTA烧录，ESP32接收到升级指令后从服务器获取升级软件包，校验后通过串口发送给STM32，STM32不预留烧录口，保证安全性

# PCB及设备规格

设备应挂在1.7m处

## 长宽高

长度根据PCB具体情况确定

宽度120mm

厚度20mm

## 预算

外壳 10￥+esp32-c3 10￥+stm32h750 50￥+PCB打样 50￥+屏幕A（OLED） 10￥+屏幕B（TFT） 30￥=160￥

# 附加功能

签到提示（签到成功后OLED显示签到成功）或显示屏

