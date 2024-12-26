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
                .cors(cors -> cors.configure(http)) // Active la configuration CORS
                .csrf(csrf -> csrf.disable())       // Désactive CSRF pour les API REST
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers("/user/emails").hasRole("USER");
                    authorizeRequests.requestMatchers("/api/users/create").permitAll();
                    authorizeRequests.requestMatchers("/swagger-ui/**","/v3/api-docs","/user","/auth/login","/request","/request/user/{userId}").permitAll();
                    authorizeRequests.requestMatchers("/user/all");//.hasRole("admin");
                    authorizeRequests.anyRequest().authenticated();
                }).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                ).build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }
}
