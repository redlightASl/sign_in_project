package cn.edu.dlut.mail.wuchen2020.signinserver.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.util.Pair;

/**
 * 帮助查询课程的工具类, 也许以后会重构
 * 
 * @author Wu Chen
 */
public final class CourseHelper {
    private static final LocalDate TERM_START = LocalDate.of(2021, 3, 1); // LocalDate.of(0, 9, 1), LocalDate.of(0, 3, 1), LocalDate.of(0, 6, 28)
    private static final Map<Integer, Pair<LocalTime, LocalTime>> CLASSES = new LinkedHashMap<>();
    static {
        CLASSES.put(5, Pair.of(LocalTime.of(18, 0), LocalTime.of(20, 0)));
        CLASSES.put(4, Pair.of(LocalTime.of(15, 35), LocalTime.of(17, 10)));
        CLASSES.put(3, Pair.of(LocalTime.of(13, 30), LocalTime.of(15, 05)));
        CLASSES.put(2, Pair.of(LocalTime.of(10, 05), LocalTime.of(11, 45)));
        CLASSES.put(1, Pair.of(LocalTime.of(8, 0), LocalTime.of(9, 35)));
    }

    private CourseHelper() {}

    public static int getWeekFromDate(LocalDate date) {
        Period period = Period.between(TERM_START, date);
        int day = period.getDays();
        if (day >= 0) {
            return day / 7 + 1;
        }
        return 0;
    }

    public static int getDayOfWeekFromDate(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    public static Lesson getLessonFromTime(LocalTime time) {
        for (Entry<Integer, Pair<LocalTime, LocalTime>> entry : CLASSES.entrySet()) {
            LocalTime startTime = entry.getValue().getFirst();
            LocalTime endTime = entry.getValue().getSecond();
            if (time.isAfter(endTime)) {
                return new Lesson(entry.getKey(), startTime, endTime, true);
            } else if (time.isAfter(startTime)) {
                return new Lesson(entry.getKey(), startTime, endTime, false);
            }
        }
        return new Lesson();
    }

    public static class Lesson {
        private int order;
        private LocalTime startTime;
        private LocalTime endTime;
        private boolean isBreaking;
        
        public Lesson() {}

        public Lesson(int order, LocalTime startTime, LocalTime endTime, boolean isBreaking) {
            this.order = order;
            this.startTime = startTime;
            this.endTime = endTime;
            this.isBreaking = isBreaking;
        }

        public int getOrder() {
            return order;
        }

        public LocalTime getStartTime() {
            return startTime;
        }

        public LocalTime getEndTime() {
            return endTime;
        }

        public boolean isBreaking() {
            return isBreaking;
        }
    }
}
