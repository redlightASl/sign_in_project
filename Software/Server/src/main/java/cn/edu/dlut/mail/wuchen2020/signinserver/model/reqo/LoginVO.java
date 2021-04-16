package cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登录信息")
public class LoginVO {
    @Schema(description = "用户名称", required = true, example = "admin")
    private String username;
    @Schema(description = "用户密码", required = true, example = "123456")
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
