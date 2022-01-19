package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.dlut.mail.wuchen2020.signinserver.dao.AdminDAO;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.pojo.Admin;
import cn.edu.dlut.mail.wuchen2020.signinserver.model.reso.AdminInfoVO;

/**
 * 管理员的业务逻辑
 * </br>
 * 另见 {@link cn.edu.dlut.mail.wuchen2020.signinserver.controller.AdminController}
 * 
 * @author Wu Chen
 */
@Service
public class AdminService {
    @Autowired
    private AdminDAO adminDAO;

    public AdminInfoVO getAdminInfo(String username) {
        Admin admin = adminDAO.findByUsername(username);
        if (admin != null) {
            AdminInfoVO adminInfo = new AdminInfoVO();
            adminInfo.setUsername(admin.getUsername());
            return adminInfo;
        }
        return null;
    }

}
