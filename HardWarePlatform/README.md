

目录结构如下

- HardWare：原理图（和PCB layout）

- Compute：STM32协处理器部分
  - Core：主程序目录
  - Drivers：STM32 HAL库文件目录
  - MDK-ARM：keilMDK工程目录
    - *.uvprojx：keilMDK工程文件
  - *.ioc CubeMX工程文件

- Control：ESP32控制部分
  - main：主程序目录
    - include：头文件目录
  - 其他：menuconfig与cmake工具配置文件

