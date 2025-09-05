package stugapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI openAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
      .info(new Info()
        .title("Stug API")
        .description("RESTful API for Stug application")
        .version("1.0")
        .contact(new Contact()
          .name("admin")
          .email("stugappinfo@example.com")))
      .addSecurityItem(new SecurityRequirement()
        .addList(securitySchemeName))
      .components(new io.swagger.v3.oas.models.Components()
        .addSecuritySchemes(securitySchemeName,
          new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")));
  }
}
