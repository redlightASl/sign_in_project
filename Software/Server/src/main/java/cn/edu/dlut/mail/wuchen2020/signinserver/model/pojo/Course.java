package cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "start_week", nullable = false)
    private Byte startWeek;

    @Column(name = "end_week", nullable = false)
    private Byte endWeek;

    @Column(name = "weekday", nullable = false)
    private Byte weekday;

    @Column(name = "start_time", nullable = false)
    private Byte startTime;

    @Column(name = "end_time", nullable = false)
    private Byte endTime;

    @ManyToOne
    @JoinColumn(name = "teacher", referencedColumnName = "id")
    private Teacher teacher;
    
    public Course() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(byte startWeek) {
        this.startWeek = startWeek;
    }

    public byte getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(byte endWeek) {
        this.endWeek = endWeek;
    }

    public byte getWeekday() {
        return weekday;
    }

    public void setWeekday(byte weekday) {
        this.weekday = weekday;
    }

    public byte getStartTime() {
        return startTime;
    }

    public void setStartTime(byte startTime) {
        this.startTime = startTime;
    }

    public byte getEndTime() {
        return endTime;
    }

    public void setEndTime(byte endTime) {
        this.endTime = endTime;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
