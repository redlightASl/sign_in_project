package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 课节信息(用于课程表的y轴)
 * 
 * @author Wu Chen
 */
@Schema(description = "课节信息")
public class LessonTimeVO {
    @Schema(description = "第几节课")
    private int period;
    @Schema(description = "上课时间")
    private LocalTime startTime;
    @Schema(description = "下课时间")
    private LocalTime endTime;
    
    public int getPeriod() {
        return period;
    }
    
    public void setPeriod(int period) {
        this.period = period;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
