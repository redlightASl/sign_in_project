package cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo;

public class LoginVO {
    private String username;
    
    private String password;
    
    public LoginVO() {}
    
    public LoginVO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
