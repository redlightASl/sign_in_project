package cn.edu.dlut.mail.wuchen2020.signinapp.model;

public class SigninStatusTeacher {
    private Course course;
    private Integer signinCount;
    private Integer totalCount;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Integer getSigninCount() {
        return signinCount;
    }

    public void setSigninCount(Integer signinCount) {
        this.signinCount = signinCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
