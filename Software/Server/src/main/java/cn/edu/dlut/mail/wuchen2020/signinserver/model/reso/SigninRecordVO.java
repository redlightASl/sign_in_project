package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import java.util.Date;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.SigninRecord.SigninStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 签到记录信息
 * 
 * @author Wu Chen
 */
@Schema(description = "签到记录信息")
public class SigninRecordVO {
    @Schema(description = "课程信息")
    private LessonVO lesson;
    @Schema(description = "签到时间")
    private Date time;
    @Schema(description = "签到状态")
    private SigninStatus status;

    public SigninRecordVO() {}

    public LessonVO getLesson() {
        return lesson;
    }

    public void setLesson(LessonVO lesson) {
        this.lesson = lesson;
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
