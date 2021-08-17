package cn.edu.dlut.mail.wuchen2020.signinserver.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.util.Pair;

/**
 * 课程时间查询器
 * 
 * @author Wu Chen
 */
public abstract class LessonTimeQuerier {
    private static LessonTimeQuerier instance = new DemoQuerier();
    
    /**
     * 获取指定日期是本学期的第几周
     * 
     * @param date 指定的日期
     * @return 第几周
     */
    public abstract int getWeek(LocalDate date);
    
    /**
     * 获取当前是本学期的第几周
     * 
     * @return 本周为第几周
     */
    public int getWeek() {
        return getWeek(LocalDate.now());
    }
    
    /**
     * 获取本学期共多少周
     * 
     * @return 本学期的周数
     */
    public abstract int getTotalWeek();
    
    
    /**
     * 获取指定时间是这天的第几节课
     * 
     * @param time 指定的时间
     * @return 第几节课
     */
    public abstract int getPeriod(LocalTime time);
    
    /**
     * 获取现在是今天的第几节课
     * 
     * @return 第几节课
     */
    public int getPeriod() {
        return getPeriod(LocalTime.now());
    }
    
    /**
     * 获取每天共多少节课
     * 
     * @return 每天的课节数
     */
    public abstract int getTotalPeriod();
    
    /**
     * 获取某节课的上课时间
     * 
     * @param period 第几节课
     * @return 上课时间
     */
    public abstract LocalTime getStartTime(int period);
    
    /**
     * 获取某节课的下课时间
     * 
     * @param period 第几节课
     * @return 下课时间
     */
    public abstract LocalTime getEndTime(int period);
    
    /**
     * 获取所有课程的上课及下课时间
     * 
     * @return 所有课程的上课及下课时间
     */
    public abstract List<Pair<LocalTime, LocalTime>> getAllLessonTimes();
    
    /**
     * 获取查询器
     * 
     * @return 查询器实例
     */
    public static LessonTimeQuerier instance() {
        return instance;
    }
    
    /**
     * 设置查询器
     * 
     * @param querier 查询器实例
     */
    public static void setQuerier(LessonTimeQuerier querier) {
        instance = querier;
    }

    /**
     * 本项目中的示例
     * 
     * @author Wu Chen
     */
    public static class DemoQuerier extends LessonTimeQuerier {
        private static final LocalDate TERM_START = LocalDate.of(2021, 3, 1); // LocalDate.of(0, 9, 1), LocalDate.of(0, 3, 1), LocalDate.of(0, 6, 28)
        private static final List<Pair<LocalTime, LocalTime>> LESSONS = new ArrayList<>();
        static {
            LESSONS.add(Pair.of(LocalTime.of(8, 0), LocalTime.of(8, 45)));
            LESSONS.add(Pair.of(LocalTime.of(8, 50), LocalTime.of(9, 35)));
            LESSONS.add(Pair.of(LocalTime.of(10, 05), LocalTime.of(10, 50)));
            LESSONS.add(Pair.of(LocalTime.of(11, 0), LocalTime.of(11, 45)));
            LESSONS.add(Pair.of(LocalTime.of(13, 30), LocalTime.of(14, 15)));
            LESSONS.add(Pair.of(LocalTime.of(14, 20), LocalTime.of(15, 05)));
            LESSONS.add(Pair.of(LocalTime.of(15, 35), LocalTime.of(16, 20)));
            LESSONS.add(Pair.of(LocalTime.of(16, 25), LocalTime.of(17, 10)));
            LESSONS.add(Pair.of(LocalTime.of(18, 0), LocalTime.of(18, 50)));
            LESSONS.add(Pair.of(LocalTime.of(18, 55), LocalTime.of(19, 45)));
            LESSONS.add(Pair.of(LocalTime.of(19, 50), LocalTime.of(19, 40)));
        }
        
        @Override
        public int getWeek(LocalDate date) {
            long day = date.toEpochDay() - TERM_START.toEpochDay();
            if (day >= 0) {
                return (int) (day / 7 + 1);
            }
            return 0;
        }

        @Override
        public int getTotalWeek() {
            return 30;
        }

        @Override
        public int getPeriod(LocalTime time) {
            if (time.isBefore(LESSONS.get(0).getFirst())) {
                return 0;
            }
            for (int i = 0; i < LESSONS.size() - 1; i++) {
                if (time.isAfter(LESSONS.get(i).getFirst()) && time.isBefore(LESSONS.get(i + 1).getFirst())) {
                    return i + 1;
                }
            }
            return LESSONS.size();
        }

        @Override
        public int getTotalPeriod() {
            return LESSONS.size();
        }

        @Override
        public LocalTime getStartTime(int period) {
            if (period > 0 && period <= LESSONS.size()) {
                return LESSONS.get(period - 1).getFirst();
            }
            return null;
        }

        @Override
        public LocalTime getEndTime(int period) {
            if (period > 0 && period <= LESSONS.size()) {
                return LESSONS.get(period - 1).getSecond();
            }
            return null;
        }

        @Override
        public List<Pair<LocalTime, LocalTime>> getAllLessonTimes() {
            return LESSONS;
        }
    }
}
