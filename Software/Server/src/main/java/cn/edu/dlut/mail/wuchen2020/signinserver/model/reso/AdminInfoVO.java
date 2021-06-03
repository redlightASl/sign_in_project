package cn.edu.dlut.mail.wuchen2020.signinserver.model.reso;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 管理员信息
 * 
 * @author Wu Chen
 */
@Schema(description = "管理员信息")
public class AdminInfoVO {
    @Schema(description = "管理员账号", example = "admin")
    private String username;

    public AdminInfoVO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
