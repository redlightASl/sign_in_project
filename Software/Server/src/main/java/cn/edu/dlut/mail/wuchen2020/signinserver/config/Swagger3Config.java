package cn.edu.dlut.mail.wuchen2020.signinserver.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Swagger 3 配置
 * 
 * @author Wu Chen
 */
@Configuration
public class Swagger3Config {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sign In Server API")
                        .version("0.0.1-SNAPSHOT")
                        .description("签到系统后端API文档")
                        .contact(new Contact().name("Wu Chen").url("https://github.com/DawningW").email("wuchen2020@mail.dlut.edu.cn"))
                        .termsOfService("https://gitee.com/redlightasl/sign_in_project")
                        .license(new License().name("GNU General Public License v3.0").url("http://www.gnu.org/licenses/gpl-3.0.html"))
                    );
                /*
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme().type(Type.APIKEY).in(In.COOKIE).name("JSESSIONID"))
                    )
                .security(List.of(new SecurityRequirement().addList("cookieAuth")))
                */
    }
    
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .pathsToMatch("/api/**")
                .build();
    }
}
