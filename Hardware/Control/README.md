# 以ESP32实现的控制及数据上传部分

使用ESP32与服务器进行通信，并通过SPI总线控制STM32的运行状态

### ESP-IDF框架

ESP-IDF 是乐鑫官方的物联网开发框架，适用于 ESP32 和 ESP32-S 系列 SoC。它基于 C/C++ 语言提供了一个自给自足的 SDK，方便用户在这些平台上开发通用应用程序。ESP-IDF 目前已服务支持数以亿计的物联网设备，并已开发构建了多种物联网产品，例如照明、消费电子大小家电、支付终端、工控等各类物联网设备。 

本项目的ESP32部分使用ESP-IDF进行开发，保证程序可以在任何ESP32设备上运行，具有较高可移植性

## 软件功能

* 直接将STM32的图像识别结果打包加密，发送给服务器❌
* 通过WiFi在**内网**收发数据✔
* 接收来自服务器的指令并发送给STM32❌
* 通过板载WiFi执行OTA升级❌

## 硬件特点

* 独立的ESP32-WROOM-32E模块最小系统
* 独立5V转3.3V供电
* 不支持独立烧录，防止硬件端破解

## 使用方法

1. 获取[ESP-IDF](https://github.com/espressif/esp-idf)及开发工具链

2. 按[乐鑫官网](https://docs.espressif.com/projects/esp-idf/zh_CN/release-v4.1/index.html)快速开始配置开发环境

3. 在本目录下使用`idf.py menuconfig`或使用VSCode/Eclipse打开本目录后配置为ESP-IDF工程，点击下方config进行配置

4. 使用`cmake、make`工具或`idf.py build`编译生成二进制文件

6. 使用`idf.py flash`指令、`esptools`工具、`安信可固件烧录工具`等进行烧录

也可以使用其他方式进行配置、烧录，只要符合ESP32的编译烧录流程即可

详情参考官网给出的ESP-IDF使用教程

注意：为保证可移植性，本项目不提供配置好的sdkconfig文件，在编译前必须进行配置

