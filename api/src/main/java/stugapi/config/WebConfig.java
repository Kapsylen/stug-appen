package stugapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/utlagg/**")  // This will apply to all endpoints under /api/
                .allowedOrigins("http://localhost:4200")    // Allow all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(false) // Set to true if you need to support cookies/credentials
                .maxAge(3600);          // Cache CORS configuration for 1 hour (3600 seconds)
    }
}
