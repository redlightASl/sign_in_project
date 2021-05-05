/*
* WiFi and HTTP Client Settings  
* Written by Redlight
*/
#ifndef __WIFI_H
#define __WIFI_H

/* WiFi Settings */
#define TARGET_WIFI_SSID "testpoint"
#define TARGET_WIFI_PASS "abcd1234"
#define TARGET_WIFI_MAX_RETRY 5
#define SET_MAX_STA_CONN            CONFIG_MAX_STA_CONN
#define SET_IP_ADDR                 CONFIG_SERVER_IP
#define SET_ESP_WIFI_AP_CHANNEL     CONFIG_ESP_WIFI_AP_CHANNEL

/* HTTP Communication Settings */
#define SOCKET_RECEIVE_BUFFER_SIZE 256
#define WEB_SERVER "http://api.dawncraft.cc"
#define WEB_PORT "80"
#define WEB_PATH_GET_TIME "/time" 
#define WEB_PATH_POST_SIGNIN "/signin"

/* Type of Connection Settings */
#if defined(CONFIG_EXAMPLE_IPV4)
    #define HOST_IP_ADDR CONFIG_EXAMPLE_IPV4_ADDR
#elif defined(CONFIG_EXAMPLE_IPV6)
    #define MAX_IP6_ADDRS_PER_NETIF (5)
#else
    #define HOST_IP_ADDR ""
#endif

#ifdef CONFIG_EXAMPLE_CONNECT_IPV6
    #define MAX_IP6_ADDRS_PER_NETIF (5)
    #define NR_OF_IP_ADDRESSES_TO_WAIT_FOR (s_active_interfaces*2)
#if defined(CONFIG_EXAMPLE_CONNECT_IPV6_PREF_LOCAL_LINK)
    #define EXAMPLE_CONNECT_PREFERRED_IPV6_TYPE ESP_IP6_ADDR_IS_LINK_LOCAL
#elif defined(CONFIG_EXAMPLE_CONNECT_IPV6_PREF_GLOBAL)
    #define EXAMPLE_CONNECT_PREFERRED_IPV6_TYPE ESP_IP6_ADDR_IS_GLOBAL
#elif defined(CONFIG_EXAMPLE_CONNECT_IPV6_PREF_SITE_LOCAL)
    #define EXAMPLE_CONNECT_PREFERRED_IPV6_TYPE ESP_IP6_ADDR_IS_SITE_LOCAL
#elif defined(CONFIG_EXAMPLE_CONNECT_IPV6_PREF_UNIQUE_LOCAL)
    #define EXAMPLE_CONNECT_PREFERRED_IPV6_TYPE ESP_IP6_ADDR_IS_UNIQUE_LOCAL
#endif //if-elif CONFIG_EXAMPLE_CONNECT_IPV6_PREF_...
#else 
    #define NR_OF_IP_ADDRESSES_TO_WAIT_FOR (s_active_interfaces)
#endif

#ifdef CONFIG_EXAMPLE_CONNECT_IPV6
    static esp_ip6_addr_t s_ipv6_addr;
    /* types of ipv6 addresses to be displayed on ipv6 events */
    static const char *s_ipv6_addr_types[] = {
        "ESP_IP6_ADDR_IS_UNKNOWN",
        "ESP_IP6_ADDR_IS_GLOBAL",
        "ESP_IP6_ADDR_IS_LINK_LOCAL",
        "ESP_IP6_ADDR_IS_SITE_LOCAL",
        "ESP_IP6_ADDR_IS_UNIQUE_LOCAL",
        "ESP_IP6_ADDR_IS_IPV4_MAPPED_IPV6"
    };
#endif

#if CONFIG_EXAMPLE_CONNECT_WIFI
    static esp_netif_t* wifi_start(void);
    static void wifi_stop(void);
#endif

void wifi_init_sta(void);
void ESP_wifi_init(void);
void Create_HTTP_Connection(void);
void TaskUpload(void* param);
void TaskListen(void* param);

#endif