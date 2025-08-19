package com.bodaeli.spring.eshop.springboot_app_bodaeli.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve las imágenes desde "regalos-matrimonio/public/img"
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:regalos-matrimonio/public/img/");
    }
}