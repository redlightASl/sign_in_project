package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 学生信息
 * 
 * @author Wu Chen
 */
@Schema(description = "学生信息")
public class StudentInfoVO {
    @Schema(description = "学生学号", example = "20202241024")
    private String number;
    @Schema(description = "学生姓名", example = "张三")
    private String name;
    @Schema(description = "班级", example = "软2001")
    private String className;
    @Schema(description = "专业", example = "软件工程")
    private String major;
    @Schema(description = "学院", example = "软件学院")
    private String department;

    public StudentInfoVO() {}

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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
