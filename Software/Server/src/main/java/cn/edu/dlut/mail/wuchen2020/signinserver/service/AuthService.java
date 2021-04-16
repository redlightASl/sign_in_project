package cn.edu.dlut.mail.wuchen2020.signinserver.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String PASSWORD_SALT = "_WC_NB-_DLUT_EDA";
    
    public boolean login(String username, String password, int userType) {

        return true;
    }
    
    public int status() {
        return 0;
    }
    
    public boolean logout(int userType) {
        
        return true;
    }
}
