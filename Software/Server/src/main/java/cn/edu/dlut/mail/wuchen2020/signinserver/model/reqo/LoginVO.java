package cn.edu.dlut.mail.wuchen2020.signinserver.model.reqo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录请求数据
 * 
 * @author Wu Chen
 */
@Schema(description = "登录信息")
public class LoginVO {
    @NotEmpty
    @Schema(description = "用户名称", required = true, example = "admin")
    private String username;
    @NotEmpty
    @Size(min = 6)
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
