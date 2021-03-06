package cn.edu.dlut.mail.wuchen2020.signinserver.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.UserSession.UserRole;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.ResultVO;
import cn.edu.dlut.mail.wuchen2020.signinserver.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 管理员接口
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.AdminService}
 * 
 * @author Wu Chen
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "admin", description = "管理员管理接口")
public class AdminController {
    @Autowired
    public AdminService adminService;
    
    @GetMapping("/getAdminInfo")
    public Object getAdminInfo(HttpSession httpSession) {
        UserSession user = (UserSession) httpSession.getAttribute("user");
        if (user.getRole() == UserRole.ADMIN) {
            return adminService.getAdminInfo(user.getUsername());
        }
        return ResultVO.fail(1002, "没有权限");
    }
}
