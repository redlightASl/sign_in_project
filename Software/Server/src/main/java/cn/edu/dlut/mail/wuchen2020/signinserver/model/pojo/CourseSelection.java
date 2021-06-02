package cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 选课记录
 * 
 * @author Wu Chen
 */
@Entity
@Table(name = "student_course")
public class CourseSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course", referencedColumnName = "id", nullable = false)
    private Course course;

    public CourseSelection() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
