package cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 签到记录
 * 
 * @author Wu Chen
 */
@Entity
@Table(name = "signin_log")
public class SigninRecord {
    /**
     * 签到状态
     */
    public enum SigninStatus {
        SUCCESS,     // 签到成功
        NOT_SIGN_IN, // 未签到
        NO_CLASS,    // 无课
        WRONG_CLASS, // 走错教室
        TEMP_LEAVE,  // 暂离
        COME_BACK,   // 返回
        LEAVE,       // 离开
        ERROR        // 错误
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student", referencedColumnName = "id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course", referencedColumnName = "id", nullable = false)
    private Course course;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "time", nullable = false)
    private Date time;

    @Column(name = "status", columnDefinition = "BIT", nullable = false)
    private SigninStatus status;

    public SigninRecord() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public SigninStatus getStatus() {
        return status;
    }

    public void setStatus(SigninStatus status) {
        this.status = status;
    }
}
