package com.billing.Invoizo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties({InvoizoProperties.class})
public class InvoizoApplication extends SpringBootServletInitializer implements WebMvcConfigurer {

    @Autowired
    private InvoizoProperties invoizoProperties;

    public static void main(String[] args) {
        SpringApplication.run(InvoizoApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/projectimages/**")
                .addResourceLocations(invoizoProperties.getImagesPathGet());
    }

}
