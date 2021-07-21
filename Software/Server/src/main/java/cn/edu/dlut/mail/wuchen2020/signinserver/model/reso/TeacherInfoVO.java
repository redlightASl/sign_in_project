package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 教师信息
 * 
 * @author Wu Chen
 */
@Schema(description = "教师信息")
public class TeacherInfoVO {
    @Schema(description = "教师工号", example = "20001001024")
    private String number;
    @Schema(description = "教师姓名", example = "王老师")
    private String name;
    @Schema(description = "班主任所带班级", example = "软2001")
    private String className;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
