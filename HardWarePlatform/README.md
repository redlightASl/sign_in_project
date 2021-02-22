

目录结构如下

- SCHDOC：原理图和PCB layout
  - *.PrjPcb：AD工程文件

- sign_in_slave：STM32协处理器部分
  - Core：主程序目录
  - Drivers：STM32 HAL库文件目录
  - MDK-ARM：keilMDK工程目录
    - *.uvprojx：keilMDK工程文件
  - *.ioc CubeMX工程文件

- sign_in_trans：ESP32控制部分
  - main：主程序目录
    - include：头文件目录
  - 其他：menuconfig与cmake工具配置文件

