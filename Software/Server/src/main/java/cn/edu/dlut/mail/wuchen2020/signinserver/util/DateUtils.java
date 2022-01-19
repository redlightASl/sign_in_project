package cn.edu.dlut.mail.wuchen2020.signinserver.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 日期时间处理辅助类
 * 
 * @author Wu Chen
 */
public final class DateUtils {
    private DateUtils() {}
    
    public static int getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }
    
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
