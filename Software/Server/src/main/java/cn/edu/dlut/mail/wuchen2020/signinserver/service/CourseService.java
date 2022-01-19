package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.LessonTimeVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.util.LessonTimeQuerier;

/**
 * 查询学期, 课节数和上课时间等的接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.AuthController}
 * 
 * @author Wu Chen
 */
@Service
public class CourseService {
    @Autowired
    private LessonTimeQuerier querier;

    public int getWeek(HttpSession httpSession) {
        return querier.getWeek();
    }

    public int getTotalWeek(HttpSession httpSession) {
        return querier.getTotalWeek();
    }

    public int getPeriod(HttpSession httpSession) {
        return querier.getPeriod();
    }

    public int getTotalPeriod(HttpSession httpSession) {
        return querier.getTotalPeriod();
    }

    public List<LessonTimeVO> getAllLessonTimes(HttpSession httpSession) {
        List<LessonTimeVO> list = new ArrayList<>();
        int period = 1;
        for (Pair<LocalTime, LocalTime> timePair : querier.getAllLessonTimes()) {
            LessonTimeVO lessonTime = new LessonTimeVO();
            lessonTime.setPeriod(period++);
            lessonTime.setStartTime(timePair.getFirst());
            lessonTime.setEndTime(timePair.getSecond());
            list.add(lessonTime);
        }
        return list;
    }
}
