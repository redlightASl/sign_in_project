package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 签到记录信息
 * 
 * @author Wu Chen
 */
@Schema(description = "签到记录信息")
public class SigninRecordVO {
    @Schema(description = "学生姓名")
    private String studentName;
    @Schema(description = "课程名称")
    private String courseName;
    @Schema(description = "签到地点")
    private String location;
    @Schema(description = "签到时间")
    private Date time;
    @Schema(description = "签到状态")
    private int status;
    
    public SigninRecordVO() {}
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
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
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
}
