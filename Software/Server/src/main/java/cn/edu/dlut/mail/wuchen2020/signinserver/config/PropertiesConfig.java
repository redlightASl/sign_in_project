package cn.edu.dlut.mail.wuchen2020.signinserver.config;

import java.time.LocalTime;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.Jsr310Converters.StringToLocalDateConverter;
import org.springframework.data.util.Pair;

/**
 * 读取自定义配置
 * 
 * @author Wu Chen
 */
@Configuration
@EnableConfigurationProperties(SigninProperties.class)
public class PropertiesConfig {
    @Bean
    @ConfigurationPropertiesBinding
    public StringToLocalDateConverter stringToLocalDateConverter() {
        return StringToLocalDateConverter.INSTANCE;
    }
    
	@Bean
	@ConfigurationPropertiesBinding
	public StringToLessonConverter lessonConverter() {
		return new StringToLessonConverter();
	}
	
	public static class StringToLessonConverter implements Converter<String, Pair<LocalTime, LocalTime>> {
		@Override
		public Pair<LocalTime, LocalTime> convert(String source) {
			String[] times = source.split(",");
			return Pair.of(LocalTime.parse(times[0]), LocalTime.parse(times[1]));
		}
	}
}
