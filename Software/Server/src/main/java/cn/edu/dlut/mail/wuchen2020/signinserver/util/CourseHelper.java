package cn.edu.dlut.mail.wuchen2020.signinserver.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

public class CourseHelper {
    private static final LocalDate[] TERMS = {LocalDate.of(0, 9, 1), LocalDate.of(0, 3, 1), LocalDate.of(0, 6, 28)};
    private static final Map<Integer, Pair<LocalTime, LocalTime>> CLASSES = new HashMap<>();
    {
        CLASSES.put(1, Pair.of(LocalTime.of(8, 0), LocalTime.of(9, 35)));
        CLASSES.put(2, Pair.of(LocalTime.of(10, 05), LocalTime.of(11, 45)));
        CLASSES.put(3, Pair.of(LocalTime.of(13, 30), LocalTime.of(15, 05)));
        CLASSES.put(4, Pair.of(LocalTime.of(15, 35), LocalTime.of(17, 10)));
        CLASSES.put(5, Pair.of(LocalTime.of(18, 0), LocalTime.of(20, 0)));
    }
    
    private CourseHelper() {}
    
    public static int getWeekFromDate(LocalDate date) {
        int day = date.getDayOfYear();
        int schoolDay = 0;
        if (day >= TERMS[0].getDayOfYear()) {
            schoolDay = day - TERMS[0].getDayOfYear();
        } else if (day <= TERMS[1].getDayOfYear()) {
            schoolDay = 365 - TERMS[0].getDayOfYear() + day;
        } else if (day <= TERMS[2].getDayOfYear()) {
            schoolDay = day - TERMS[1].getDayOfYear();
        } else if (day <= TERMS[0].getDayOfYear()) {
            schoolDay = day - TERMS[2].getDayOfYear();
        }
        return schoolDay / 7 + 1;
    }
    
    public static int getDayOfWeekFromDate(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }
    
    public static int getClassFromTime(LocalTime time) {
        for (Entry<Integer, Pair<LocalTime, LocalTime>> entry : CLASSES.entrySet()) {
            if (time.isAfter(entry.getValue().getLeft()) && time.isBefore(entry.getValue().getRight())) {
                return entry.getKey();
            }
        }
        return 0;
    }
}
