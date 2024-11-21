package com.example.springboot.config;

import com.example.springboot.auth.Oauth2AuthenticationEntrypoint;
import com.example.springboot.auth.Oauth2LoginSuccessHandler;
import com.example.springboot.auth.Oauth2LogoutSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Oauth2LoginSuccessHandler loginSuccessHandler;
    private final Oauth2LogoutSuccessHandler logoutSuccessHandler;
    private final HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL));

    public SecurityConfig(final Oauth2LoginSuccessHandler loginSuccessHandler, final Oauth2LogoutSuccessHandler logoutSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    // https://spring.io/guides/tutorials/spring-boot-oauth2
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#security-matchers
            .authorizeHttpRequests(authorize -> authorize
                // add public routes
                .requestMatchers("/").permitAll()
                .requestMatchers("/logout").permitAll()
                .requestMatchers("/oauth2/authorization/google").permitAll()
                .anyRequest().authenticated()
            )
            // https://github.com/spring-projects/spring-security/issues/4315
            // @fixme enable again, this fails to create the XSRF_TOKEN cookie?
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2Login(customizer -> customizer.successHandler(loginSuccessHandler))
            // https://docs.spring.io/spring-security/reference/servlet/authentication/logout.html#clear-all-site-data
            .logout((logout) -> {
                logout.permitAll();
                logout.addLogoutHandler(clearSiteData);
                logout.clearAuthentication(true);
                logout.invalidateHttpSession(true);
                // https://baeldung.com/spring-security-disable-logout-redirects
                logout.logoutSuccessHandler(logoutSuccessHandler);
            })
            .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(
                new Oauth2AuthenticationEntrypoint()))
        ;
        return http.build();
    }
}
