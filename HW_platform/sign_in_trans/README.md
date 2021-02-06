# 以ESP32实现的控制及数据上传部分

使用ESP32与服务器进行通信、控制摄像头，并通过SPI总线控制STM32的运行状态

## 软件功能

* 直接将STM32的图像识别结果打包加密，发送给服务器
* 通过WiFi相关协议在**内网**收发数据
* 接收来自服务器的指令并发送给STM32
* 通过板载WiFi执行OTA升级


## 硬件特点

* 独立的ESP32-WROOM-32E模块最小系统
* 独立5V转3.3V供电
* 不支持独立烧录，防止硬件端破解

## 使用方法

1. 从https://github.com/espressif/esp-idf获取ESP-IDF及开发工具链

2. 按说明配置开发环境

3. 将本目录下文件放在项目文件夹中

4. 新建build目录并使用`cmake`工具或`idf.py menuconfig`进行配置

6. 使用`idf.py build`指令和`esptools`工具进行编译、烧录

也可以使用VSCode及ESP32插件进行自动配置、烧录，只要符合ESP32的编译流程即可




