package cn.edu.dlut.mail.wuchen2020.signinserver;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 大学生创新创业实践项目 - 人脸识别课堂签到系统后端
 * 
 * @version 0.0.1
 * @author Luo Tian
 * @author Liu Yuan
 * @author Wu Chen
 */
@ServletComponentScan("cn.edu.dlut.mail.wuchen2020.signinserver.filter")
@SpringBootApplication
public class SignInServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignInServerApplication.class);
    private static ApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(SignInServerApplication.class, args);
    }
    
    public static ApplicationContext getContext() {
        return context;
    }
    
    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }
    
    @Component
    public static class SigninServerApplicationRunner implements ApplicationRunner {
        @Autowired
        Environment environment;
        
        @Override
        public void run(ApplicationArguments args) throws Exception {
            LOGGER.info("人脸识别课堂签到系统后端初始化完成");
            LOGGER.info("作者: Luo Tian, Liu Yuan, Wu Chen");
            String host = InetAddress.getLocalHost().getHostAddress();
            String port = environment.getProperty("server.port");
            LOGGER.info("访问http://{}:{}/swagger-ui.html查看API文档", host, port);
        }
    }
}
