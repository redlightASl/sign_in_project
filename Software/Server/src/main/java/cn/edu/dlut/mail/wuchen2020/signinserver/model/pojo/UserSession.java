package cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo;

import java.util.Date;

/**
 * 用户会话, 存储在HttpSession里
 * </br>
 * TODO 存储在数据库中以便后端重启后保持会话
 * 
 * @author Wu Chen
 */
public class UserSession {
    /**
     * 用户角色(学生/教师/管理员)
     */
    public enum UserRole {
        STUDENT,
        TEACHER,
        ADMIN
    }
    
    private String username;
    private String session;
    private Date expire;
    private UserRole role;

    public UserSession() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
