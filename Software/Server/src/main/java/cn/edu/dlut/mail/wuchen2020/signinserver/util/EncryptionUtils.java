package cn.edu.dlut.mail.wuchen2020.signinserver.util;

import org.springframework.util.DigestUtils;

/**
 * 加密算法工具类
 * 
 * @author Wu Chen
 */
public final class EncryptionUtils {
    private EncryptionUtils() {}
    
    public static String encodeMD5(String salt, String password) {
        String saltPassword = salt + password;
        String md5Password = DigestUtils.md5DigestAsHex(saltPassword.getBytes());
        return md5Password;
    }
}
