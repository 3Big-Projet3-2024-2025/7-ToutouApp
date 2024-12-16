package be.projet3.toutouapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // Autorise toutes les origines
        config.addAllowedHeader("*");        // Autorise tous les en-têtes
        config.addAllowedMethod("*");        // Autorise toutes les méthodes (GET, POST, etc.)
        config.setAllowCredentials(true);    // Autorise les cookies si besoin

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Applique CORS à toutes les routes
        return new CorsFilter(source);
    }
}
