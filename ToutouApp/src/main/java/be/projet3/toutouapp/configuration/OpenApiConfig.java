package be.projet3.toutouapp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI (Swagger) documentation for the API.
 * This class defines the custom configuration for OpenAPI 3.0, including API metadata, security settings,
 * and the security scheme for Bearer token authentication using JWT.
 * The configuration will be used to generate and document the API endpoints automatically.
 * @see  be.projet3.toutouapp.configuration
 * @author Damien DeLeeuw
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI documentation with custom information and security settings.
     * This method defines the title, version, and security configuration for the API.
     * It adds support for Bearer token authentication (JWT) for the endpoints.
     *
     * @return an instance of {@link OpenAPI} with the custom configuration
     * @author Damien DeLeeuw
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API Documentation").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
