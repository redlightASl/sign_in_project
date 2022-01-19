package cn.edu.dlut.mail.wuchen2020.signinserver.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import cn.edu.dlut.mail.wuchen2020.signinserver.SignInServerApplication;
import cn.edu.dlut.mail.wuchen2020.signinserver.config.SigninProperties;

/**
 * 课程时间查询器
 * 
 * @author Wu Chen
 */
public abstract class LessonTimeQuerier {
    private static LessonTimeQuerier instance;

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
        if (instance == null) {
            instance = SignInServerApplication.getBean(ConfiguredQuerier.class);
        }
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
     * 从配置文件中读取的课程时间查询器
     * 
     * @author Wu Chen
     */
    @Component
    public static class ConfiguredQuerier extends LessonTimeQuerier {
        private LocalDate termStartTime;
        private List<Pair<LocalTime, LocalTime>> lessonTimes;
        private int totalWeekCount;

        public ConfiguredQuerier(@Autowired SigninProperties signinProperties) {
            termStartTime = signinProperties.getTermStartTime();
            lessonTimes = signinProperties.getLessonTimes();
            totalWeekCount = signinProperties.getTotalWeekCount();
        }

        @Override
        public int getWeek(LocalDate date) {
            long day = date.toEpochDay() - termStartTime.toEpochDay();
            if (day >= 0) {
                return (int) (day / 7 + 1);
            }
            return 0;
        }

        @Override
        public int getTotalWeek() {
            return totalWeekCount;
        }

        @Override
        public int getPeriod(LocalTime time) {
            if (time.isBefore(lessonTimes.get(0).getFirst())) {
                return 0;
            }
            for (int i = 0; i < lessonTimes.size() - 1; i++) {
                if (time.isAfter(lessonTimes.get(i).getFirst()) && time.isBefore(lessonTimes.get(i + 1).getFirst())) {
                    return i + 1;
                }
            }
            return lessonTimes.size();
        }

        @Override
        public int getTotalPeriod() {
            return lessonTimes.size();
        }

        @Override
        public LocalTime getStartTime(int period) {
            if (period > 0 && period <= lessonTimes.size()) {
                return lessonTimes.get(period - 1).getFirst();
            }
            return null;
        }

        @Override
        public LocalTime getEndTime(int period) {
            if (period > 0 && period <= lessonTimes.size()) {
                return lessonTimes.get(period - 1).getSecond();
            }
            return null;
        }

        @Override
        public List<Pair<LocalTime, LocalTime>> getAllLessonTimes() {
            return lessonTimes;
        }
    }
}
