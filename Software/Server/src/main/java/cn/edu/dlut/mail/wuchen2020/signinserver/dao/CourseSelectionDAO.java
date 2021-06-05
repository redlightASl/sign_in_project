package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.CourseSelection;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Student;

public interface CourseSelectionDAO extends JpaRepository<CourseSelection, Long> {
    List<CourseSelection> findByStudent(Student student);
    
    @Query(value =
            "SELECT sc.id, sc.student, sc.course FROM student_course AS sc "
            + "INNER JOIN course AS c ON sc.course = c.id "
            + "WHERE sc.student = ?1 AND c.start_week <= ?2 AND c.end_week >= ?2 AND c.weekday = ?3 AND c.start_time <= ?4 AND c.end_time >= ?4 ;",
            nativeQuery = true)
    CourseSelection findCourseSelected(int studentId, int week, int dayOfWeek, int time);
}
