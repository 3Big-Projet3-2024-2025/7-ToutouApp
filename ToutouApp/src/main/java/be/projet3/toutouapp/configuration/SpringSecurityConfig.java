package be.projet3.toutouapp.configuration;

import be.projet3.toutouapp.security.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration class for setting up security filters and rules in the application.
 * This class configures CORS, CSRF, and request authorization rules based on user roles (USER, ADMIN) for different endpoints.
 * It also integrates with Keycloak for JWT-based authentication, utilizing {@link KeycloakRoleConverter} for role mapping.
 * @author Damien DeLeeuw
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    /**
     * Configures the security filter chain for the application, including CORS, CSRF settings,
     * and authorization rules for different endpoints based on user roles.
     * <p>
     * - CORS is enabled and configured using the global CORS settings.
     * - CSRF is disabled to allow for stateless authentication in REST APIs.
     * - Specific endpoints are restricted to users with the "ADMIN" role or "USER"/"ADMIN" roles.
     * - Swagger documentation endpoints are made publicly accessible.
     * - JWT authentication is set up using a custom {@link KeycloakRoleConverter} for mapping roles.
     * </p>
     *
     * @param http the {@link HttpSecurity} object for configuring HTTP security
     * @return a configured {@link SecurityFilterChain} to manage security for the application
     * @throws Exception if the configuration fails
     * @author Sirjacques Célestin
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configure(http)) // Enables CORS configuration
                .csrf(csrf -> csrf.disable())       // Disables CSRF for REST APIs
                .authorizeRequests(authorizeRequests -> {
                    // Accessible only by ADMIN
                    authorizeRequests.requestMatchers(
                            "/user/{id}/block",
                            "/user/{id}/flag",
                            "/rating/negative",
                            "/rating/{id}"
                    ).hasRole("ADMIN");


                    // Accessible only by USER or ADMIN
                    authorizeRequests.requestMatchers(
                            "/user/**",
                            "/request/**",
                            "/rating/**",
                            "/messages/**",
                            "/api/**",
                            "/chats/**"
                    ).hasAnyRole("USER", "ADMIN");

                    // Accessible by everyone
                    authorizeRequests.requestMatchers(
                            "/swagger-ui/**",
                            "/v3/api-docs"
                    ).permitAll();

                    // Authentication required for all other requests

                    authorizeRequests.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }


    /**
     * Creates a custom {@link JwtAuthenticationConverter} to convert the JWT token into authenticated user roles.
     * The converter is configured to use a custom {@link KeycloakRoleConverter} to map the roles in the JWT token.
     *
     * @return a {@link JwtAuthenticationConverter} configured with a custom role converter
     * @author Damien DeLeeuw
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
