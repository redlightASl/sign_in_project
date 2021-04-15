package cn.edu.dlut.mail.wuchen2020.signinserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder().username("admin").password("123456").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
        /*
        http
        // 关闭csrf防护
        .csrf().disable()
        .headers().frameOptions().disable()
        .and()
        // 授权配置
        .authorizeRequests()
        .antMatchers("/", "/swagger-ui.html", "/swagger-ui/**", "/v3/**")
        .permitAll()
        .anyRequest().authenticated()
        .and()
        // 登录
        .formLogin()
        .loginPage("/login.html")
        .loginProcessingUrl("/user/login")
        .defaultSuccessUrl("/")
        .permitAll()
        .and()
        // 注销
        .logout()
        .permitAll();
        */
    }
}
