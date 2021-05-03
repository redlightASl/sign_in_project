# 原理图与PCB

使用AltiumDesigner绘制

本项目的LAYOUT暂时不开源（其实是不确定要不要单独打板）

可使用ESP32和STM32对应的最小系统板制作项目的电路部分

# 硬件配置

## 基本需求

1. STM32H750zg开发板/核心板

2. ESP32-WROOM-32系列模组或开发板

## 引脚配置

### ESP32-WROOM-32

1. SPI：与协处理器通信

使用SPI2 **HSPI**配置为标准SPI主模式，**仅使用下面的SPICLK、SPICS0、SPIQ、SPID**，列出所有SPI引脚供参考

| 引脚名 | SPIHD | SPIWP | SPICS0 | SPICLK | SPIQ | SPID |
| ------ | ----- | ----- | ------ | ------ | ---- | ---- |
| 编号   | 28    | 29    | 20     | 21     | 32   | 33   |


2. UART：执行OTA升级

| 引脚名 | U0RXD | U0TXD |
| ------ | ----- | ----- |
| 编号   | 40    | 41    |


2. 摄像头接口

采用ESP-WHO框架`CONFIG_CAMERA_MODEL_WROVER_KIT`下的摄像头接口实现

| 引脚名         | 编号     |
| -------------- | -------- |
| PWDN_GPIO_NUM  | 无（-1） |
| RESET_GPIO_NUM | 无（-1） |
| XCLK_GPIO_NUM  | 21       |
| SIOD_GPIO_NUM  | 26       |
| SIOC_GPIO_NUM  | 27       |
| Y9_GPIO_NUM    | 35       |
| Y8_GPIO_NUM    | 34       |
| Y7_GPIO_NUM    | 39       |
| Y6_GPIO_NUM    | 36       |
| Y5_GPIO_NUM    | 19       |
| Y4_GPIO_NUM    | 18       |
| Y3_GPIO_NUM    | 5        |
| Y2_GPIO_NUM    | 4        |
| VSYNC_GPIO_NUM | 25       |
| HREF_GPIO_NUM  | 23       |
| PCLK_GPIO_NUM  | 22       |

也可以采用STM32的DCMI接口实现

| 功能       | 对应引脚 |
| ---------- | -------- |
| DCMI_HSYNC | PA4      |
| DCMI_PCLK  | PA6      |
| DCMI_XCLK  | PA8      |
| DCMI_SDA   | PB3      |
| DCMI_SCL   | PB4      |
| DCMI_VSYNC | PB7      |
| DCMI_D6    | PB8      |
| DCMI_D7    | PB9      |
| DCMI_D0    | PC6      |
| DCMI_D1    | PC7      |
| DCMI_D2    | PC8      |
| DCMI_D3    | PC9      |
| DCMI_D4    | PC11     |
| DCMI_D5    | PD3      |
| DCMI_RESET | PG12     |


3. 供电

![image-20210224011040081](C:\Users\NH55\AppData\Roaming\Typora\typora-user-images\image-20210224011040081.png)

使用独立的5V转3.3V LDO提供3.3V工作电压，并在模组上加装散热片保证工作温度不高于85℃，也可以选用高温版本的ESP32模组


4. 烧录接口

使用ESP32-DevKitS将ESP32模组烧录完成后焊接到PCB上，不预留烧录接口、不允许二次烧录，考虑使用官方提供的加密API进一步保证安全性

### STM32H750VBT6

1. SPI：与控制器通信

使用SPI1配置为标准SPI从模式

| 引脚名 | SPI1_SCK | SPI1_MISO | SPI1_MOSI |
| ------ | -------- | --------- | --------- |
| 编号   | PA5      | PA6       | PA7       |

2. UART：执行OTA升级

使用USART0，配置为IAP模式并写入IAP引导程序

| 引脚名 | U0Tx | U0Rx |
| ------ | ---- | ---- |
| 编号   | PA9  | PA10 |


2. 供电

使用独立的3.3V LDO供电，不单独提供备份时钟域供电，但出于安全性考虑加入应急断电保护电路，以保证断电情况下设备可以将最后得出数据发送给ESP32且能够接收来自ESP32的休眠指令

![image-20210224015156411](C:\Users\NH55\AppData\Roaming\Typora\typora-user-images\image-20210224015156411.png)

3. 烧录接口

本项目使用UART对STM32执行OTA烧录，ESP32接收到升级指令后从服务器获取升级软件包，校验后通过串口发送给STM32，STM32不预留烧录口，保证安全性

# PCB大小

挂在1.7m处

长宽高：

长度根据PCB具体情况确定

宽度120mm

厚度20mm

价格：外壳 10￥+esp32-c3 10￥+stm32h750 50￥+PCB打样 50￥+屏幕A（OLED） 10￥+屏幕B（TFT） 30￥=160￥

# 功能

签到提示（签到成功后OLED显示签到成功）或显示屏

