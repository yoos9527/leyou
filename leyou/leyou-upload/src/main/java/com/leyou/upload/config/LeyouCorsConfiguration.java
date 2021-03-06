package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class LeyouCorsConfiguration {
    @Bean
    public CorsFilter corsFilter(){
        //初始化cors配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        //允许跨域的域名，若要携带Cookie,不能写 *  *代表任何域名都可跨域访问
        configuration.addAllowedOrigin("http://manage.leyou.com");
        configuration.setAllowCredentials(true);//可以携带cookie
        configuration.addAllowedMethod("*");//代表所有请求方法
        configuration.addAllowedHeader("*");//可以携带任何头信息
        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource configyrationSource = new UrlBasedCorsConfigurationSource();
        configyrationSource.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(configyrationSource);
    }
}
