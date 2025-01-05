package be.projet3.toutouapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration class for global Cross-Origin Resource Sharing (CORS) settings.
 * This class defines a {@link CorsFilter} bean to allow cross-origin requests from any domain, with support for all HTTP methods and headers.
 * It ensures that the CORS settings are applied globally across all endpoints in the application.
 * @see  be.projet3.toutouapp.configuration
 * @author Damien DeLeeuw
 */
@Configuration
public class GlobalCorsConfig {


    /**
     * Defines a {@link CorsFilter} bean to configure global CORS settings.
     * The CORS filter allows cross-origin requests from any domain, supports all HTTP methods (GET, POST, etc.),
     * allows all headers, and permits credentials to be included in the requests.
     * This filter is applied to all paths (/**).
     *
     * @return a {@link CorsFilter} instance with the configured CORS settings
     * @author Damien DeLeeuw
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
