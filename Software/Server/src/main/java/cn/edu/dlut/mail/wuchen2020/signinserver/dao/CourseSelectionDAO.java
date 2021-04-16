package cn.edu.dlut.mail.wuchen2020.signinserver.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.CourseSelection;

public interface CourseSelectionDAO extends JpaRepository<CourseSelection, Long> {
    // TODO 还没实现根据时间查询
    @Query(value = "SELECT sc.id, sc.student, sc.course FROM student_course AS sc INNER JOIN course AS c ON sc.course = c.id WHERE sc.student = ?1 ;", nativeQuery = true)
    CourseSelection findCourseSelected(int studentId, int week, int dayOfWeek, int time);
}
