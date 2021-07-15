package cn.edu.dlut.mail.wuchen2020.signinapp.model;

public class User {

    private String userId;
    private String displayName;

    public User(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}