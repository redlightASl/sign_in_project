#ifndef __APP_WEB_H
#define __APP_WEB_H

#define WEB_SERVER "signin.dawncraft.cc"
#define WEB_SERVER_GET_URL "signin.dawncraft.cc/api/time"
#define WEB_SERVER_POST_URL "http://signin.dawncraft.cc/api/signin"
#define WEB_PORT "80"
#define WEB_PATH_ROOT "/"
#define WEB_PATH_GET_TIME "/api/time"
#define WEB_PATH_POST_SIGNIN "/api/signin"
#define WEB_QUERY "esp"

void esp_wait_sntp_sync(void);
void Get_timestamp_task(void* pvParameters);
void Post_data_task(void* pvParameters);
#endif
