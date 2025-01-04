package be.projet3.toutouapp.configuration;

import be.projet3.toutouapp.security.KeycloakRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

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

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
