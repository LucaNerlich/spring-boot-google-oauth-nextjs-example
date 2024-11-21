package com.example.springboot.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@EnableJpaAuditing
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configures a FormatterRegistry with custom formatters.
     *
     * <p>This method registers a custom ApplicationConversionService with the FormatterRegistry.</p>
     *
     * <p>Further documentation links</p>
     * <ol>
     *     <li><a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/format/annotation/FormatterRegistry.html">FormatterRegistry - Spring Framework</a></li>
     *      <li><a href="https://www.baeldung.com/spring-boot-enum-mapping">Spring Boot Enum Mapping</a></li>
     * </ol>
     *
     * @param registry the FormatterRegistry to be configured
     * @see FormatterRegistry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }

    /**
     * Configures and registers a rate-limiting filter using a token bucket algorithm for incoming HTTP requests.
     * The filter applies to all URL patterns and limits the number of requests per IP address.
     *
     * @return a FilterRegistrationBean configured with the BucketFilter
     */
    @Bean
    public FilterRegistrationBean<BucketFilter> setupRateLimitBucketFilter() {
        final FilterRegistrationBean<BucketFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new BucketFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) settings for the application.
     * This method sets allowed origins, methods, and configurations for handling cross-origin requests.
     *
     * @return a UrlBasedCorsConfigurationSource configured with CORS settings
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList(
            "https://*.example.com",
            "http://localhost:3000"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Content-Type"));
        configuration.setMaxAge(3600L);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
