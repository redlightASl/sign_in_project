package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 课程信息
 * 
 * @author Wu Chen
 */
@Schema(description = "课程信息")
public class LessonVO {
    @Schema(description = "课序号")
    private int id;
    @Schema(description = "课程名称")
    private String name;
    @Schema(description = "上课地点")
    private String location;
    @Schema(description = "授课教师姓名")
    private String teacherName;
    @Schema(description = "星期几上课")
    private int dayOfWeek;
    @Schema(description = "开始节数")
    private int startTime;
    @Schema(description = "结束节数")
    private int endTime;
    
    public int getID() {
        return id;
    }
    
    public void setID(int id) {
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
    
    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
