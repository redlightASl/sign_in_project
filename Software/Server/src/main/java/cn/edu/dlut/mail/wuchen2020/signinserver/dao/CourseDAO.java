package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Course;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Teacher;

public interface CourseDAO extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(Teacher teacher);
    @Query(value = "SELECT * FROM course AS c WHERE c.teacher = ?1 AND c.start_week <= ?2 AND c.end_week >= ?2 AND c.weekday = ?3 AND c.start_time <= ?4 AND c.end_time >= ?4 ;", nativeQuery = true)
    Course findCourseTeached(int teacherId, int week, int dayOfWeek, int period);
}
