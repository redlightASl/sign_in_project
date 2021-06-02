package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

/**
 * 教师信息
 * 
 * @author Wu Chen
 */
public class TeacherInfoVO {
    private String number;
    private String name;
    private String className;

    public TeacherInfoVO() {}

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
