package com.hms.HospitalMS.Config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI hospitalOpenAPI() {
        return new OpenAPI().info(new Info()
            .title("Hospital Management System API")
            .description("API for Hospital Management System")
            .version("1.0.0"));
    }
}
