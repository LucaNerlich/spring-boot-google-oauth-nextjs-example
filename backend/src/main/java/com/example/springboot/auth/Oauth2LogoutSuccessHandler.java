package com.example.springboot.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Oauth2LogoutSuccessHandler implements LogoutSuccessHandler {

    @Value("${COOKIE_DOMAIN:localhost:3000}")
    private String cookieDomain;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication) {
        final Cookie sessionCookie = new Cookie("JSESSIONID", null);
        sessionCookie.setDomain(cookieDomain);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);

        final Cookie csrfCookie = new Cookie("XSRF-TOKEN", null);
        csrfCookie.setDomain(cookieDomain);
        csrfCookie.setMaxAge(0);
        csrfCookie.setPath("/");
        csrfCookie.setHttpOnly(true);
        csrfCookie.setSecure(true);

        response.addCookie(sessionCookie);
        response.addCookie(csrfCookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
